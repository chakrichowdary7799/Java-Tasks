package com.example.ecommerce_cart_checkout.service;

import com.example.ecommerce_cart_checkout.dto.request.CheckoutRequest;
import com.example.ecommerce_cart_checkout.dto.response.OrderSummaryResponse;

public interface CheckoutService {
    OrderSummaryResponse checkout(Long userId, CheckoutRequest request);
}