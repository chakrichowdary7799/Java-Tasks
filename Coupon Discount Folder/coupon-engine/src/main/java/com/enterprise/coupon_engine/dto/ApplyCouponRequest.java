package com.enterprise.coupon_engine.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplyCouponRequest {
    @NotNull(message = "Order ID is mandatory")
    private Long orderId;

    @NotEmpty(message = "Provide at least one coupon code")
    private List<String> couponCodes;
}
