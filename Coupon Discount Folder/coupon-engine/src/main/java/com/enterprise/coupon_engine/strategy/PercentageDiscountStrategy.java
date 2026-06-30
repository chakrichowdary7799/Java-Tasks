package com.enterprise.coupon_engine.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import com.enterprise.coupon_engine.entity.Coupon;
import com.enterprise.coupon_engine.entity.CouponType;
import com.enterprise.coupon_engine.entity.Order;

@Component
public class PercentageDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal calculateDiscount(Order order, Coupon coupon) {
        BigDecimal percentage = coupon.getDiscountValue().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal discount = order.getTotalAmount().multiply(percentage);
        return order.getTotalAmount().min(discount).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public CouponType getSupportedType() {
        return CouponType.PERCENTAGE;
    }
}
