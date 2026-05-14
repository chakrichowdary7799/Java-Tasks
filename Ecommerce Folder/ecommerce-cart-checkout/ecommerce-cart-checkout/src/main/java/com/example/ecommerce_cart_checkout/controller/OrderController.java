package com.example.ecommerce_cart_checkout.controller;

import com.example.ecommerce_cart_checkout.dto.response.OrderHistoryResponse;
import com.example.ecommerce_cart_checkout.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders") //http://localhost:8080/api/orders
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping("/{userId}") //http://localhost:8080/api/orders/1
    public ResponseEntity<Page<OrderHistoryResponse>> getOrderHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
	return ResponseEntity.ok(orderService.getOrderHistory(userId, PageRequest.of(page, size)));
    }
}