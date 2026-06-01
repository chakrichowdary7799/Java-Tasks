package com.example.apigateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GatewayController {

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> simulateUserService() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "User Microservice Instance");
        response.put("status", "UP");
        response.put("data", new String[]{"UserA_Admin", "UserB_Client", "UserC_Manager"});
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> simulateOrderService() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Order Microservice Instance");
        response.put("status", "UP");
        response.put("data", new String[]{"Order_#99201", "Order_#99202", "Order_#99203"});
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> simulateProductService() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Product Microservice Instance");
        response.put("status", "UP");
        response.put("data", new String[]{"Enterprise_Server_Node", "Fiber_Optic_Transceiver"});
        return ResponseEntity.ok(response);
    }
}