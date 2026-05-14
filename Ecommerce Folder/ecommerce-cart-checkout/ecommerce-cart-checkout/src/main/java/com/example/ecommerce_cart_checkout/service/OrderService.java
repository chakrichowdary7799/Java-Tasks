package com.example.ecommerce_cart_checkout.service;

import com.example.ecommerce_cart_checkout.dto.response.OrderHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderHistoryResponse> getOrderHistory(Long userId, Pageable pageable);
}