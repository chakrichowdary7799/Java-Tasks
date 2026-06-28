package com.example.order.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.common.event.OrderStatus;
import com.example.common.event.PaymentEvent;
import com.example.order.repository.OrderRepository;

@Component
public class PaymentResultListener {

    private final OrderRepository orderRepository;

    // Explicit constructor fixes the initialization compile error
    public PaymentResultListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "payment-events", groupId = "order-group")
    public void handlePayment(PaymentEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            if ("SUCCESSFUL".equalsIgnoreCase(event.getStatus())) {
                order.setStatus(OrderStatus.COMPLETED);
            } else {
                order.setStatus(OrderStatus.REJECTED);
            }
            orderRepository.save(order);
        });
    }
}