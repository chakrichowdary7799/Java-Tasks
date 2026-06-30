package com.enterprise.coupon_engine.dto;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private BigDecimal originalAmount;
    private BigDecimal finalAmount;
    private BigDecimal totalDiscount;
    private Map<String, BigDecimal> appliedCoupons;
}
