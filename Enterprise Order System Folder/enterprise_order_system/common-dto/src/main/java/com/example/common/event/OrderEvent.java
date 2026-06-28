package com.example.common.event;

import java.math.BigDecimal;

public class OrderEvent {
    private String eventId;
    private Long orderId;
    private Long userId;
    private BigDecimal total;
    private OrderStatus status;

    // Standard Default No-Argument Constructor
    public OrderEvent() {
    }

    // Explicit All-Arguments Constructor to fix the compilation error
    public OrderEvent(String eventId, Long orderId, Long userId, BigDecimal total, OrderStatus status) {
        this.eventId = eventId;
        this.orderId = orderId;
        this.userId = userId;
        this.total = total;
        this.status = status;
    }

    // --- Standard Getters and Setters ---

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}