package com.user.user_service;

public record UserRequestDTO(
    String username,
    String email,
    String password,
    String fullName
) {}
