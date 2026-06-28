package com.example.order.messaging;

import com.example.common.event.OrderStatus;
import com.example.common.event.PaymentEvent;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentResultListener {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment-events", groupId = "order-group")
    public void handlePayment(PaymentEvent event) {
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus("SUCCESSFUL".equals(event.getStatus()) ? OrderStatus.COMPLETED : OrderStatus.REJECTED);
            orderRepository.save(order);
        });
    }
}