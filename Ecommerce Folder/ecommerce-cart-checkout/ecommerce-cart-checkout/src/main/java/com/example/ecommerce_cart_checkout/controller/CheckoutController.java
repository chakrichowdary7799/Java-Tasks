package com.example.ecommerce_cart_checkout.controller;

import com.example.ecommerce_cart_checkout.dto.request.CheckoutRequest;
import com.example.ecommerce_cart_checkout.dto.response.OrderSummaryResponse;
import com.example.ecommerce_cart_checkout.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout") //http://localhost:8080/api/checkout
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) { this.checkoutService = checkoutService; }

    @PostMapping("/{userId}") //http://localhost:8080/api/checkout/1
    public ResponseEntity<OrderSummaryResponse> checkout(@PathVariable Long userId, @RequestBody(required = false) CheckoutRequest request) {
        if (request == null) request = new CheckoutRequest();
        return ResponseEntity.ok(checkoutService.checkout(userId, request));
    }
}