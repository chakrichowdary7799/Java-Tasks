package com.enterprise.coupon_engine.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.enterprise.coupon_engine.dto.ApplyCouponRequest;
import com.enterprise.coupon_engine.entity.Coupon;
import com.enterprise.coupon_engine.entity.CouponType;
import com.enterprise.coupon_engine.entity.Order;
import com.enterprise.coupon_engine.repository.CouponRepository;
import com.enterprise.coupon_engine.repository.OrderRepository;

@SpringBootTest
public class CheckoutServiceConcurrencyTest {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @SuppressWarnings({ "UseSpecificCatch", "null" })
    public void testConcurrentCouponApplicationExhaustion() throws InterruptedException {
        // Initialize structural shared data
        @SuppressWarnings("unused")
        Coupon targetCoupon = couponRepository.save(Coupon.builder()
                .code("CONC10")
                .type(CouponType.FLAT)
                .discountValue(BigDecimal.valueOf(10.00))
                .usageLimit(1) // Only one order thread should successfully claim this coupon
                .build());

        Order testOrder = orderRepository.save(Order.builder()
                .userId(999L)
                .totalAmount(BigDecimal.valueOf(100.00))
                .build());

        int simulatedThreadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(simulatedThreadCount);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successfulRedemptions = new AtomicInteger(0);
        AtomicInteger failedRedemptions = new AtomicInteger(0);

        for (int i = 0; i < simulatedThreadCount; i++) {
            executor.submit(() -> {
                try {
                    latch.await(); // Synchronize all worker threads to fire simultaneously
                    ApplyCouponRequest request = new ApplyCouponRequest();
                    request.setOrderId(testOrder.getId());
                    request.setCouponCodes(Collections.singletonList("CONC10"));
                    
                    checkoutService.applyCoupons(request);
                    successfulRedemptions.incrementAndGet();
                } catch (Exception e) {
                    failedRedemptions.incrementAndGet();
                }
            });
        }

        latch.countDown(); // Release lock gates for execution burst
        executor.shutdown();
        Thread.sleep(3000); // Allow calculations to resolve completely

        // Assertions confirm strict transaction isolation behavior
        assertEquals(1, successfulRedemptions.get(), "Only 1 transaction thread must successfully capture row scope");
        assertEquals(9, failedRedemptions.get(), "Remaining 9 parallel attempts should fail cleanly via exceptions");
    }
}
