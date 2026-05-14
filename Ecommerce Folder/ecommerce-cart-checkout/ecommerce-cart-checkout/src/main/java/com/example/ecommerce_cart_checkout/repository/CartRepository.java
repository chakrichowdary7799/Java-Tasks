package com.example.ecommerce_cart_checkout.repository;

import com.example.ecommerce_cart_checkout.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}