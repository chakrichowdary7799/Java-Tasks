package com.enterprise.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.enterprise.security.context.TenantContext;
import com.enterprise.security.service.JwtUtil;
import com.enterprise.security.service.TokenBlacklistService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklistService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenBlacklistService blacklistService) {
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/auth/"); 
    }

    @Override
    @SuppressWarnings("UseSpecificCatch")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String tenantHeader = request.getHeader("X-Tenant-ID");

        if (tenantHeader != null) {
            TenantContext.setCurrentTenant(tenantHeader);
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (!blacklistService.isTokenBlacklisted(token)) {
                    String username = jwtUtil.extractUsername(token);
                    String tokenTenantId = jwtUtil.extractTenantId(token);

                    if (tenantHeader != null && !tenantHeader.equals(tokenTenantId)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid Tenant Isolation Scope.");
                        return;
                    }

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        Claims claims = jwtUtil.extractAllClaims(token);
                        List<?> roles = claims.get("roles", List.class);
                        List<?> permissions = claims.get("permissions", List.class);

                        List<SimpleGrantedAuthority> authorities = roles.stream()
                                .map(r -> new SimpleGrantedAuthority(r.toString())).collect(Collectors.toList());
                        
                        if (permissions != null) {
                            permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.toString())));
                        }

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                username, null, authorities);
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear(); 
        }
    }
}
