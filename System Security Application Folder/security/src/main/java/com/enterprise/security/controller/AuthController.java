package com.enterprise.security.controller;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.security.context.TenantContext;
import com.enterprise.security.dto.AuthResponse;
import com.enterprise.security.dto.LoginRequest;
import com.enterprise.security.entity.Permission;
import com.enterprise.security.entity.RefreshToken;
import com.enterprise.security.entity.Role;
import com.enterprise.security.entity.User;
import com.enterprise.security.repository.PermissionRepository;
import com.enterprise.security.repository.RefreshTokenRepository;
import com.enterprise.security.repository.RoleRepository;
import com.enterprise.security.repository.UserRepository;
import com.enterprise.security.service.JwtUtil;
import com.enterprise.security.service.TokenBlacklistService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklistService;
    private final RoleRepository roleRepository;
    private final PermissionRepository permRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, UserRepository userRepo, 
                          RefreshTokenRepository tokenRepo, JwtUtil jwtUtil, TokenBlacklistService blacklistService,
                          RoleRepository roleRepository, PermissionRepository permRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authManager;
        this.userRepository = userRepo;
        this.tokenRepository = tokenRepo;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
        this.roleRepository = roleRepository;
        this.permRepository = permRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/setup")
    public ResponseEntity<String> setupTestData() {
        if (userRepository.findByTenantIdAndUsername("tenant_company_a", "john_doe").isPresent()) {
            return ResponseEntity.ok("Database setup already initialized!");
        }

        Permission readUser = new Permission();
        readUser.setPermissionName("READ_USER");
        readUser = permRepository.save(readUser);

        Role adminRole = new Role();
        adminRole.setRoleName("ROLE_ADMIN");
        adminRole.setPermissions(Set.of(readUser));
        adminRole = roleRepository.save(adminRole);

        User user = new User();
        user.setUsername("john_doe");
        user.setPassword(passwordEncoder.encode("secure123")); 
        user.setTenantId("tenant_company_a");
        user.setRoles(Set.of(adminRole));
        userRepository.save(user);

        return ResponseEntity.ok("Database seeded successfully! User: john_doe, Password: secure123");
    }

    @PostMapping("/login")
    @SuppressWarnings("UseSpecificCatch")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, @RequestHeader("X-Tenant-ID") String tenantId) {
        TenantContext.setCurrentTenant(tenantId);
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            TenantContext.clear();
            return ResponseEntity.status(403).body("Authentication Failed: " + e.getMessage());
        }

        User user = userRepository.findByTenantIdAndUsername(tenantId, request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<String> roles = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList());
        List<String> permissions = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream()).map(Permission::getPermissionName).collect(Collectors.toList());

        String access = jwtUtil.generateToken(user.getUsername(), tenantId, roles, permissions);
        String refresh = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(refresh);
        refreshToken.setExpiryDate(Instant.now().plusMillis(604800000));
        tokenRepository.save(refreshToken);

        return ResponseEntity.ok(new AuthResponse(access, refresh));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistService.blacklistToken(token, 3600000);
        }
        return ResponseEntity.ok("Logged out successfully.");
    }
}
