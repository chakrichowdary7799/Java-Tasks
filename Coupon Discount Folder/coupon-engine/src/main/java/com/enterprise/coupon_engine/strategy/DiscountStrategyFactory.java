package com.enterprise.coupon_engine.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enterprise.coupon_engine.entity.CouponType;
import com.enterprise.coupon_engine.exception.CouponException;

@Component
public class DiscountStrategyFactory {

    private final Map<CouponType, DiscountStrategy> strategies = new HashMap<>();

    public DiscountStrategyFactory(List<DiscountStrategy> strategyList) {
        for (DiscountStrategy strategy : strategyList) {
            strategies.put(strategy.getSupportedType(), strategy);
        }
    }

    public DiscountStrategy getStrategy(CouponType type) {
        DiscountStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new CouponException("Unsupported strategy type: " + type);
        }
        return strategy;
    }
}
