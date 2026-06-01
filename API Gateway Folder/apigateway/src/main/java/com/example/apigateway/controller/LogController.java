package com.example.apigateway.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.apigateway.entity.RequestLog;
import com.example.apigateway.service.LogService;

@RestController
@RequestMapping("/api")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    public ResponseEntity<List<RequestLog>> fetchGatewayAuditLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }
}