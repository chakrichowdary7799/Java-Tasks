package com.example.order.messaging;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.entity.OutboxEvent;
import com.example.order.repository.OutboxRepository;

@Component
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // Explicit constructor initializes final variables cleanly without Lombok
    public OutboxScheduler(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void dispatch() {
        List<OutboxEvent> pending = outboxRepository.findByProcessedFalse();
        for (OutboxEvent event : pending) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getPayload()).get();
                event.setProcessed(true);
                outboxRepository.save(event);
            } catch (Exception e) {
                System.err.println("Transactional Outbox Alert: Kafka engine unreachable. Postponing drop sync.");
            }
        }
    }
}