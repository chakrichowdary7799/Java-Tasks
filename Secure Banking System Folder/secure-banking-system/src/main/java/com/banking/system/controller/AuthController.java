package com.banking.system.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // Simple validation fallback for localized execution without structural database dependencies
        if ("bank_user".equals(username) && "password123".equals(password)) {
            Map<String, String> response = new HashMap<>();
            response.put("token", "mock-jwt-token-for-testing");
            response.put("status", "Authenticated Successfully");
            return ResponseEntity.ok(response);
        }
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized: Invalid username or password");
        return ResponseEntity.status(401).body(errorResponse);
    }
}