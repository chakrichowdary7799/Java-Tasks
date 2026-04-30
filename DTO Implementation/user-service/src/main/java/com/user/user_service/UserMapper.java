package com.user.user_service;

public class UserMapper {
    public static UserResponseDTO toResponseDTO(UserEntity entity) {
        return new UserResponseDTO(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getFullName()
        );
    }

    public static UserEntity toEntity(UserRequestDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.username());
        entity.setEmail(dto.email());
        entity.setFullName(dto.fullName());
        entity.setPassword(dto.password()); 
        return entity;
    }
}

