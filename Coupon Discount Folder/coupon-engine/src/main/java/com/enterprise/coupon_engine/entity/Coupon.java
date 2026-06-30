package com.enterprise.coupon_engine.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupons", indexes = {@Index(name = "idx_coupon_code", columnList = "code")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coupon code cannot be empty")
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull(message = "Coupon type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @NotNull(message = "Discount value is required")
    @Min(value = 0, message = "Discount value must be positive")
    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    @FutureOrPresent(message = "Expiry date must be in the present or future")
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Min(value = 0, message = "Usage limit must be zero or positive")
    @Column(name = "usage_limit")
    private Integer usageLimit;
}
