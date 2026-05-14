package com.example.ecommerce_cart_checkout.dto.response;

import java.math.BigDecimal;

public class OrderSummaryResponse {
    private Long orderId;
    private BigDecimal finalAmount;
    private String paymentStatus;

    public OrderSummaryResponse(Long orderId, BigDecimal finalAmount, String paymentStatus) {
        this.orderId = orderId;
        this.finalAmount = finalAmount;
        this.paymentStatus = paymentStatus;
    }

    public Long getOrderId() { return orderId; }
    public BigDecimal getFinalAmount() { return finalAmount; }
    public String getPaymentStatus() { return paymentStatus; }
}
