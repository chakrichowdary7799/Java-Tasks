package com.example.ecommerce_cart_checkout.service.impl;

import com.example.ecommerce_cart_checkout.dto.response.OrderHistoryResponse;
import com.example.ecommerce_cart_checkout.repository.OrderRepository;
import com.example.ecommerce_cart_checkout.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<OrderHistoryResponse> getOrderHistory(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(order -> new OrderHistoryResponse(
                        order.getId(),
                        order.getTotalAmount(),
                        order.getStatus(),
                        order.getCreatedAt()
                ));
    }
}