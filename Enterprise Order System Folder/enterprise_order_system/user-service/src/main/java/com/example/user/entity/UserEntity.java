package com.example.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused") // Explicitly clears the VS Code field analysis warning
    private Long id;
    
    private String email;
    private String fullName;

    // Default No-Argument Constructor
    public UserEntity() {
    }

    // Explicit All-Arguments Constructor
    public UserEntity(Long id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }

    // --- Standard Explicit Getters and Setters ---

    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getFullName() { 
        return fullName; 
    }
    
    public void setFullName(String fullName) { 
        this.fullName = fullName; 
    }
}