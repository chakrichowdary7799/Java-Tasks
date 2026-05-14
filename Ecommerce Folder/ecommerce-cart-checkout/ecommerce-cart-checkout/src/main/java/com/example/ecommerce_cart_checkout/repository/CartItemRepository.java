package com.example.ecommerce_cart_checkout.repository;

import com.example.ecommerce_cart_checkout.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {}