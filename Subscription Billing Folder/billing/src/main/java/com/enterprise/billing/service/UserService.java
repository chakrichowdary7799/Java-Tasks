package com.enterprise.billing.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enterprise.billing.dto.request.UserRequest;
import com.enterprise.billing.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    UserResponse getUserById(Long id);
    Page<UserResponse> getAllUsers(String search, Pageable pageable);
    void deleteUser(Long id);
}