package com.example.userapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}