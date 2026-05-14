package com.example.ecommerce_cart_checkout.service;

import com.example.ecommerce_cart_checkout.dto.request.CartItemRequest;
import com.example.ecommerce_cart_checkout.dto.response.CartResponse;

public interface CartService {
    CartResponse addToCart(Long userId, CartItemRequest request);
    CartResponse updateQuantity(Long userId, Long productId, int quantity);
    CartResponse removeFromCart(Long userId, Long productId);
    CartResponse viewCart(Long userId);
}