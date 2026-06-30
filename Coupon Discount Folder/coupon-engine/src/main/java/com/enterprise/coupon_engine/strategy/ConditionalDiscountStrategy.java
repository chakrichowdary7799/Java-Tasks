package com.enterprise.coupon_engine.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.enterprise.coupon_engine.entity.Coupon;
import com.enterprise.coupon_engine.entity.CouponType;
import com.enterprise.coupon_engine.entity.Order;

@Component
public class ConditionalDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(Order order, Coupon coupon) {
        // Conditional: Eligible for discount value only if order amount >= 500
        BigDecimal minimumThreshold = BigDecimal.valueOf(500.00);
        if (order.getTotalAmount().compareTo(minimumThreshold) >= 0) {
            return order.getTotalAmount().min(coupon.getDiscountValue());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public CouponType getSupportedType() {
        return CouponType.CONDITIONAL;
    }
}
