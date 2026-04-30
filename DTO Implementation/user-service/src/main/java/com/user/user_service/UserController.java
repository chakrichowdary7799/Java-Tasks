package com.user.user_service;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO requestDTO) {
        // 1. Map DTO to Entity
        UserEntity entity = UserMapper.toEntity(requestDTO);
        
        // 2. Simulate saving to DB (setting a dummy ID)
        entity.setId(101L);
        
        // 3. Map Entity back to Response DTO and return
        return UserMapper.toResponseDTO(entity);
    }
}

