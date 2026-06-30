package com.enterprise.coupon_engine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enterprise.coupon_engine.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // N+1 Prevention: Uses FETCH JOIN to load discounts and orders in a single database round-trip
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDiscounts WHERE o.id = :id")
    Optional<Order> findByIdWithDiscounts(@Param("id") Long id);
}
