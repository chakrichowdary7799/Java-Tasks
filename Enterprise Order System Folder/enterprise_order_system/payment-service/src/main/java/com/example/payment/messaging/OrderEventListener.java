package com.example.payment.messaging;

import com.example.common.event.OrderEvent;
import com.example.common.event.PaymentEvent;
import com.example.payment.entity.ProcessedMessage;
import com.example.payment.repository.ProcessedMessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OrderEventListener {

    private final ProcessedMessageRepository messageRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Explicit constructor initializes variables perfectly, satisfying the compiler
    public OrderEventListener(ProcessedMessageRepository messageRepository, 
                              KafkaTemplate<String, Object> kafkaTemplate) {
        this.messageRepository = messageRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    @Transactional
    public void consumeOrder(OrderEvent event) {
        // Idempotency Deduplication check (using plain Java getters)
        if (messageRepository.existsById(event.getEventId())) {
            return; 
        }

        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setEventId(UUID.randomUUID().toString());
        paymentEvent.setOrderId(event.getOrderId());
        paymentEvent.setStatus("SUCCESSFUL");
        paymentEvent.setPaymentId((long) (Math.random() * 100000));

        messageRepository.save(new ProcessedMessage(event.getEventId(), LocalDateTime.now()));
        kafkaTemplate.send("payment-events", paymentEvent);
    }
}