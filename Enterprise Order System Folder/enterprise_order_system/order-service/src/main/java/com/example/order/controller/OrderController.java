package com.example.order.controller;

import com.example.order.entity.OrderEntity;
import com.example.order.repository.OrderRepository;
import com.example.order.entity.OutboxEvent;
import com.example.order.repository.OutboxRepository;
import com.example.common.event.OrderEvent;
import com.example.common.event.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    // Explicit constructor initializes variables perfectly, satisfying the compiler
    public OrderController(OrderRepository orderRepository, 
                           OutboxRepository outboxRepository, 
                           ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@RequestBody OrderEntity order) throws Exception {
        Optional<OrderEntity> existing = orderRepository.findByIdempotencyKey(order.getIdempotencyKey());
        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }
        
        order.setStatus(OrderStatus.PENDING);
        OrderEntity savedOrder = orderRepository.save(order);

        OrderEvent event = new OrderEvent(
            UUID.randomUUID().toString(), 
            savedOrder.getId(), 
            savedOrder.getUserId(), 
            savedOrder.getTotal(), 
            savedOrder.getStatus()
        );
        
        OutboxEvent outbox = new OutboxEvent();
        outbox.setTopic("order-events");
        outbox.setPayload(objectMapper.writeValueAsString(event));
        outboxRepository.save(outbox);

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}