package com.enterprise.warehouse.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enterprise.warehouse.model.Allocation;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    @Query("SELECT a FROM Allocation a WHERE " +
           "(:productId IS NULL OR a.productId = :productId) AND " +
           "(:warehouseId IS NULL OR a.warehouseId = :warehouseId) AND " +
           "(a.allocatedAt BETWEEN :startDate AND :endDate)")
    Page<Allocation> searchAllocations(
            @Param("productId") Long productId,
            @Param("warehouseId") Long warehouseId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}