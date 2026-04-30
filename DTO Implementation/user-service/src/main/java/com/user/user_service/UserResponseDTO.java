package com.user.user_service;

public record UserResponseDTO(
    Long id,
    String username,
    String email,
    String fullName
) {}
