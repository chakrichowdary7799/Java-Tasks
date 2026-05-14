package com.example.ecommerce_cart_checkout.repository;

import com.example.ecommerce_cart_checkout.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}