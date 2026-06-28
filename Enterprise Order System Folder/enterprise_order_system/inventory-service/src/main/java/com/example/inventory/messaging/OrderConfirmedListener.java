package com.example.inventory.messaging;

import com.example.common.event.OrderEvent;
import com.example.common.event.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConfirmedListener {
    @KafkaListener(topics = "order-events", groupId = "inventory-group")
    public void handleOrderStockUpdate(OrderEvent event) {
        if (OrderStatus.COMPLETED.equals(event.getStatus())) {
            System.out.println("Processing event details for completed order ID: " + event.getOrderId());
        }
    }
}