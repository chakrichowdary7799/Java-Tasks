package com.enterprise.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedResourceController {

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('READ_USER')")
    public String getUsersDashboard() {
        return "Access Granted: Validated custom JWT token. Displaying Multi-Tenant User CRUD Viewports.";
    }
}
