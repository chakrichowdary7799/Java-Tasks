package com.enterprise.coupon_engine.strategy;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.enterprise.coupon_engine.entity.Coupon;
import com.enterprise.coupon_engine.entity.CouponType;
import com.enterprise.coupon_engine.entity.Order;

public class DiscountStrategyTest {

    private DiscountStrategyFactory strategyFactory;

    @BeforeEach
    public void setUp() {
        // Build factory manually using individual strategy beans to optimize test runtime
        strategyFactory = new DiscountStrategyFactory(Arrays.asList(
                new FlatDiscountStrategy(),
                new PercentageDiscountStrategy(),
                new ConditionalDiscountStrategy()
        ));
    }

    @Test
    public void testFlatDiscountStrategy_CalculatesCorrectly() {
        DiscountStrategy strategy = strategyFactory.getStrategy(CouponType.FLAT);
        
        Order order = Order.builder().totalAmount(BigDecimal.valueOf(100.00)).build();
        Coupon coupon = Coupon.builder().type(CouponType.FLAT).discountValue(BigDecimal.valueOf(20.00)).build();

        BigDecimal discount = strategy.calculateDiscount(order, coupon);
        assertEquals(0, BigDecimal.valueOf(20.00).compareTo(discount), "Flat discount should subtract matching value");
    }

    @Test
    public void testFlatDiscountStrategy_CapAtOrderTotal() {
        DiscountStrategy strategy = strategyFactory.getStrategy(CouponType.FLAT);
        
        Order order = Order.builder().totalAmount(BigDecimal.valueOf(15.00)).build();
        Coupon coupon = Coupon.builder().type(CouponType.FLAT).discountValue(BigDecimal.valueOf(50.00)).build();

        BigDecimal discount = strategy.calculateDiscount(order, coupon);
        assertEquals(0, BigDecimal.valueOf(15.00).compareTo(discount), "Flat discount must never exceed total order basket value");
    }

    @Test
    public void testPercentageDiscountStrategy_CalculatesCorrectly() {
        DiscountStrategy strategy = strategyFactory.getStrategy(CouponType.PERCENTAGE);
        
        Order order = Order.builder().totalAmount(BigDecimal.valueOf(250.00)).build();
        Coupon coupon = Coupon.builder().type(CouponType.PERCENTAGE).discountValue(BigDecimal.valueOf(10.00)).build(); // 10%

        BigDecimal discount = strategy.calculateDiscount(order, coupon);
        assertEquals(0, BigDecimal.valueOf(25.00).compareTo(discount), "10% of 250 must evaluate exactly to 25.00");
    }

    @Test
    public void testConditionalDiscountStrategy_EligibleWhenAboveThreshold() {
        DiscountStrategy strategy = strategyFactory.getStrategy(CouponType.CONDITIONAL);
        
        Order order = Order.builder().totalAmount(BigDecimal.valueOf(600.00)).build(); // Target condition limit threshold is 500
        Coupon coupon = Coupon.builder().type(CouponType.CONDITIONAL).discountValue(BigDecimal.valueOf(75.00)).build();

        BigDecimal discount = strategy.calculateDiscount(order, coupon);
        assertEquals(0, BigDecimal.valueOf(75.00).compareTo(discount), "Discount rules apply because order amount exceeds 500 threshold");
    }

    @Test
    public void testConditionalDiscountStrategy_DeniedWhenBelowThreshold() {
        DiscountStrategy strategy = strategyFactory.getStrategy(CouponType.CONDITIONAL);
        
        Order order = Order.builder().totalAmount(BigDecimal.valueOf(350.00)).build(); // Fails structural limit threshold check
        Coupon coupon = Coupon.builder().type(CouponType.CONDITIONAL).discountValue(BigDecimal.valueOf(75.00)).build();

        BigDecimal discount = strategy.calculateDiscount(order, coupon);
        assertEquals(0, BigDecimal.ZERO.compareTo(discount), "Discount returns 0 because order value sits below criteria requirement");
    }
}
