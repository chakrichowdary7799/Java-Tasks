package com.enterprise.warehouse.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enterprise.warehouse.dto.AllocationRequest;
import com.enterprise.warehouse.dto.AllocationResponse;
import com.enterprise.warehouse.dto.StockTransferRequest;
import com.enterprise.warehouse.model.Allocation;

public interface AllocationService {
    AllocationResponse allocateStock(AllocationRequest request);
    void transferStock(StockTransferRequest request);
    Page<Allocation> searchAllocations(Long productId, Long warehouseId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}