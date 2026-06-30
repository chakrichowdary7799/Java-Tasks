package com.enterprise.coupon_engine.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.coupon_engine.dto.ApplyCouponRequest;
import com.enterprise.coupon_engine.dto.OrderResponse;
import com.enterprise.coupon_engine.entity.Coupon;
import com.enterprise.coupon_engine.entity.Order;
import com.enterprise.coupon_engine.entity.OrderDiscount;
import com.enterprise.coupon_engine.entity.UserCoupon;
import com.enterprise.coupon_engine.exception.CouponException;
import com.enterprise.coupon_engine.repository.CouponRepository;
import com.enterprise.coupon_engine.repository.OrderRepository;
import com.enterprise.coupon_engine.repository.UserCouponRepository;
import com.enterprise.coupon_engine.strategy.DiscountStrategyFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final DiscountStrategyFactory strategyFactory;

    @Transactional
    public OrderResponse applyCoupons(ApplyCouponRequest request) {
        // Fetch order with N+1 optimized fetch join
        Order order = orderRepository.findByIdWithDiscounts(request.getOrderId())
                .orElseThrow(() -> new CouponException("Order tracking ID not found"));

        // Safeguard Idempotency: Clear previous calculations if client re-triggers coupon application
        order.getOrderDiscounts().clear();

        BigDecimal runningTotal = order.getTotalAmount();
        BigDecimal cumulativeDiscount = BigDecimal.ZERO;
        Map<String, BigDecimal> snapshotApplied = new HashMap<>();

        for (String code : request.getCouponCodes()) {
            // Apply pessimistic lock boundary to isolate evaluation state safely
            Coupon coupon = couponRepository.findByCodeWithLock(code)
                    .orElseThrow(() -> new CouponException("Coupon code mapping is invalid: " + code));

            validateCouponEligibility(coupon, order.getUserId());

            // Deduce precise deduction value using selected strategy component
            BigDecimal itemizedReduction = strategyFactory.getStrategy(coupon.getType())
                    .calculateDiscount(order, coupon);

            if (itemizedReduction.compareTo(BigDecimal.ZERO) > 0) {
                runningTotal = runningTotal.subtract(itemizedReduction);
                cumulativeDiscount = cumulativeDiscount.add(itemizedReduction);
                snapshotApplied.put(coupon.getCode(), itemizedReduction);

                // Commit transactional trace log record
                OrderDiscount orderDiscount = OrderDiscount.builder()
                        .coupon(coupon)
                        .discountApplied(itemizedReduction)
                        .build();
                order.addDiscount(orderDiscount);

                // Update structural concurrency tracking states safely
                trackAndDeductUsage(coupon, order.getUserId());
            }
        }

        orderRepository.save(order);

        return OrderResponse.builder()
                .orderId(order.getId())
                .originalAmount(order.getTotalAmount())
                .finalAmount(runningTotal.max(BigDecimal.ZERO))
                .totalDiscount(cumulativeDiscount)
                .appliedCoupons(snapshotApplied)
                .build();
    }

    private void validateCouponEligibility(Coupon coupon, Long userId) {
        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CouponException("The provided coupon is expired: " + coupon.getCode());
        }

        if (coupon.getUsageLimit() != null && coupon.getUsageLimit() <= 0) {
            throw new CouponException("The global limit for coupon has been exhausted: " + coupon.getCode());
        }

        // Enforce structural unique single-use checks inside database row scope
        userCouponRepository.findByUserIdAndCouponIdWithLock(userId, coupon.getId())
                .ifPresent(uc -> {
                    if (uc.isUsedFlag()) {
                        throw new CouponException("This single-use coupon has already been redeemed: " + coupon.getCode());
                    }
                });
    }

    private void trackAndDeductUsage(Coupon coupon, Long userId) {
        if (coupon.getUsageLimit() != null) {
            coupon.setUsageLimit(coupon.getUsageLimit() - 1);
            couponRepository.save(coupon);
        }

        UserCoupon userRegistration = userCouponRepository.findByUserIdAndCouponIdWithLock(userId, coupon.getId())
                .orElseGet(() -> UserCoupon.builder()
                        .userId(userId)
                        .coupon(coupon)
                        .usedFlag(false)
                        .build());

        userRegistration.setUsedFlag(true);
        userCouponRepository.save(userRegistration);
    }
}
