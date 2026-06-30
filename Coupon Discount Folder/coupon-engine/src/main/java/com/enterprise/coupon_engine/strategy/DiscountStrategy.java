package com.enterprise.coupon_engine.strategy;

import java.math.BigDecimal;

import com.enterprise.coupon_engine.entity.Coupon;
import com.enterprise.coupon_engine.entity.CouponType;
import com.enterprise.coupon_engine.entity.Order;

public interface DiscountStrategy {
    BigDecimal calculateDiscount(Order order, Coupon coupon);
    CouponType getSupportedType();
}
