package com.example.ecommerce_cart_checkout.repository;

import com.example.ecommerce_cart_checkout.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, String> {}