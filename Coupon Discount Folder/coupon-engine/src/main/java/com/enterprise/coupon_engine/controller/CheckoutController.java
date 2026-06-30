package com.enterprise.coupon_engine.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.coupon_engine.dto.ApplyCouponRequest;
import com.enterprise.coupon_engine.dto.OrderResponse;
import com.enterprise.coupon_engine.service.CheckoutService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;
    
    @PostMapping("/apply-coupons")
    public ResponseEntity<OrderResponse> applyCoupons(@Valid @RequestBody ApplyCouponRequest request) {
        OrderResponse response = checkoutService.applyCoupons(request);
        return ResponseEntity.ok(response);
    }

}

