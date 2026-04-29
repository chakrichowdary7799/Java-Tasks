package com.validationproject.validation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok("User is valid!");
    }
}

