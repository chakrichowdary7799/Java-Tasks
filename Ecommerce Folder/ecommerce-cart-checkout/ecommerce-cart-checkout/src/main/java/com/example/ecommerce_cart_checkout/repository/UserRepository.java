package com.example.ecommerce_cart_checkout.repository;

import com.example.ecommerce_cart_checkout.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
