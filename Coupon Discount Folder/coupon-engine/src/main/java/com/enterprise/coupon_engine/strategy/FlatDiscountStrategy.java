package com.enterprise.coupon_engine.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.enterprise.coupon_engine.entity.Coupon;
import com.enterprise.coupon_engine.entity.CouponType;
import com.enterprise.coupon_engine.entity.Order;

@Component
public class FlatDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(Order order, Coupon coupon) {
        // Flat discount cannot exceed the order amount
        return order.getTotalAmount().min(coupon.getDiscountValue());
    }

    @Override
    public CouponType getSupportedType() {
        return CouponType.FLAT;
    }
}
