package com.example.common.event;

public class PaymentEvent {
    private String eventId;
    private Long paymentId;
    private Long orderId;
    private String status;

    public PaymentEvent() {
    }

    public PaymentEvent(String eventId, Long paymentId, Long orderId, String status) {
        this.eventId = eventId;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.status = status;
    }

    // --- Standard Getters and Setters ---

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
