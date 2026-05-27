package com.enterprise.billing.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.billing.dto.request.UserRequest;
import com.enterprise.billing.dto.response.UserResponse;
import com.enterprise.billing.entity.AuditLog;
import com.enterprise.billing.entity.User;
import com.enterprise.billing.exception.ResourceNotFoundException;
import com.enterprise.billing.repository.AuditLogRepository;
import com.enterprise.billing.repository.UserRepository;
import com.enterprise.billing.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public UserServiceImpl(UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        
        User savedUser = userRepository.save(user);
        auditLogRepository.save(new AuditLog("CREATE_USER", getCurrentUser(), "Created user with ID: " + savedUser.getId()));
        return mapToResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        
        User updatedUser = userRepository.save(user);
        auditLogRepository.save(new AuditLog("UPDATE_USER", getCurrentUser(), "Updated user with ID: " + id));
        return mapToResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(String search, Pageable pageable) {
        Page<User> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return users.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        auditLogRepository.save(new AuditLog("DELETE_USER", getCurrentUser(), "Deleted user with ID: " + id));
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}