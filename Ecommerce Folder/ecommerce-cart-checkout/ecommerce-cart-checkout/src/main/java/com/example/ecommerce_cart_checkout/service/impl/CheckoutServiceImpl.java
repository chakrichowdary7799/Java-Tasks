package com.example.ecommerce_cart_checkout.service.impl;

import com.example.ecommerce_cart_checkout.dto.request.CheckoutRequest;
import com.example.ecommerce_cart_checkout.dto.response.OrderSummaryResponse;
import com.example.ecommerce_cart_checkout.exception.InsufficientStockException;
import com.example.ecommerce_cart_checkout.exception.ResourceNotFoundException;
import com.example.ecommerce_cart_checkout.entities.*;
import com.example.ecommerce_cart_checkout.repository.*;
import com.example.ecommerce_cart_checkout.service.CheckoutService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final Random random = new Random();

    public CheckoutServiceImpl(CartRepository cartRepository, ProductRepository productRepository, 
                               OrderRepository orderRepository, CouponRepository couponRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    @Transactional
    public OrderSummaryResponse checkout(Long userId, CheckoutRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart absolute empty or not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot checkout an empty cart");
        }

        // 1. Stock Evaluation Strategy
        BigDecimal rawTotal = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for: " + product.getName());
            }
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            rawTotal = rawTotal.add(subtotal);
        }

        // 2. Application of Discount Strategy
        BigDecimal finalAmount = rawTotal;
        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            Coupon coupon = couponRepository.findById(request.getCouponCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon invalid"));
            
            if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Coupon expired");
            }

            if ("FLAT".equalsIgnoreCase(coupon.getDiscountType())) {
                finalAmount = finalAmount.subtract(coupon.getDiscountValue());
            } else if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
                BigDecimal factor = coupon.getDiscountValue().divide(BigDecimal.valueOf(100));
                finalAmount = finalAmount.subtract(rawTotal.multiply(factor));
            }
            if (finalAmount.compareTo(BigDecimal.ZERO) < 0) finalAmount = BigDecimal.ZERO;
        }

        // 3. Persistent Order Instantiation
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(finalAmount);
        order.setStatus("PENDING");

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        }
        orderRepository.save(order);

        // 4. Reduction of Product Inventory Strategy
        for (CartItem item : cart.getItems()) {
            Product p = item.getProduct();
            p.setStockQuantity(p.getStockQuantity() - item.getQuantity());
            productRepository.save(p);
        }

        // 5. Payment Sandbox Execution Gateway
        boolean paymentSuccess = simulatePaymentGateway();

        if (paymentSuccess) {
            order.setStatus("SUCCESS");
            orderRepository.save(order);
            cart.getItems().clear();
            cartRepository.save(cart);
            return new OrderSummaryResponse(order.getId(), finalAmount, "SUCCESS");
        } else {
            // Rollback inventory manipulation if checkout payment simulation drops out
            order.setStatus("FAILED");
            orderRepository.save(order);
            for (CartItem item : cart.getItems()) {
                Product p = item.getProduct();
                p.setStockQuantity(p.getStockQuantity() + item.getQuantity());
                productRepository.save(p);
            }
            return new OrderSummaryResponse(order.getId(), finalAmount, "FAILED");
        }
    
    }
    private boolean simulatePaymentGateway() {
        return random.nextDouble() > 0.15; // 85% success simulation profile
    }
}