package com.enterprise.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enterprise.warehouse.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}