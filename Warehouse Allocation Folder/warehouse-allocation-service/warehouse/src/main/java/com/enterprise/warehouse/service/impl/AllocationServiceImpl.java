package com.enterprise.warehouse.service.impl;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.warehouse.dto.AllocationRequest;
import com.enterprise.warehouse.dto.AllocationResponse;
import com.enterprise.warehouse.dto.StockTransferRequest;
import com.enterprise.warehouse.exception.InsufficientStockException;
import com.enterprise.warehouse.exception.ResourceNotFoundException;
import com.enterprise.warehouse.model.Allocation;
import com.enterprise.warehouse.model.AllocationStatus;
import com.enterprise.warehouse.model.StockTransfer;
import com.enterprise.warehouse.model.Warehouse;
import com.enterprise.warehouse.model.WarehouseInventory;
import com.enterprise.warehouse.repository.AllocationRepository;
import com.enterprise.warehouse.repository.ProductRepository;
import com.enterprise.warehouse.repository.StockTransferRepository;
import com.enterprise.warehouse.repository.WarehouseInventoryRepository;
import com.enterprise.warehouse.repository.WarehouseRepository;
import com.enterprise.warehouse.service.AllocationService;


@Service
public class AllocationServiceImpl implements AllocationService {

    @Autowired private ProductRepository productRepository;
    @Autowired private WarehouseRepository warehouseRepository;
    @Autowired private WarehouseInventoryRepository inventoryRepository;
    @Autowired private AllocationRepository allocationRepository;
    @Autowired private StockTransferRepository stockTransferRepository;

    @Override
    @Transactional
    public AllocationResponse allocateStock(AllocationRequest request) {
        if (request.getQuantity() <= 0) {
            throw new InsufficientStockException("Allocation quantity must be positive");
        }

        productRepository.findById(request.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Match automatic warehouse strategy based on available stock descending
        List<WarehouseInventory> inventoryOptions = inventoryRepository
                .findByProductIdOrderByAvailableQuantityDesc(request.getProductId());

        WarehouseInventory match = inventoryOptions.stream()
                .filter(inv -> inv.getAvailableQuantity() >= request.getQuantity())
                .findFirst()
                .orElseThrow(() -> new InsufficientStockException("Insufficient cumulative stock across eligible units"));

        // Execute changes via Optimistic Locking updates
        match.setAvailableQuantity(match.getAvailableQuantity() - request.getQuantity());
        inventoryRepository.save(match);

        Allocation log = Allocation.builder()
                .productId(request.getProductId())
                .warehouseId(match.getWarehouseId())
                .quantity(request.getQuantity())
                .allocatedAt(LocalDateTime.now())
                .status(AllocationStatus.SUCCESS)
                .build();
        allocationRepository.save(log);

        return AllocationResponse.builder()
                .allocationId(log.getId())
                .productId(log.getProductId())
                .warehouseId(log.getWarehouseId())
                .quantity(log.getQuantity())
                .status(log.getStatus().name())
                .timestamp(log.getAllocatedAt())
                .build();
    }

    @Override
    @Transactional
    public void transferStock(StockTransferRequest request) {
        WarehouseInventory sourceInv = inventoryRepository
            .findByWarehouseIdAndProductId(request.getSourceWarehouseId(), request.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Source stock record missing"));

        if (sourceInv.getAvailableQuantity() < request.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock at source warehouse");
        }

        Warehouse targetUnit = warehouseRepository.findById(request.getTargetWarehouseId())
            .orElseThrow(() -> new ResourceNotFoundException("Target warehouse not found"));

        WarehouseInventory targetInv = inventoryRepository
            .findByWarehouseIdAndProductId(request.getTargetWarehouseId(), request.getProductId())
            .orElseGet(() -> {
                WarehouseInventory newInv = new WarehouseInventory();
                newInv.setWarehouseId(targetUnit.getId());
                newInv.setProductId(request.getProductId());
                newInv.setAvailableQuantity(0);
                return newInv;
            });

        // Mutate positions
        sourceInv.setAvailableQuantity(sourceInv.getAvailableQuantity() - request.getQuantity());
        targetInv.setAvailableQuantity(targetInv.getAvailableQuantity() + request.getQuantity());

        inventoryRepository.save(sourceInv);
        inventoryRepository.save(targetInv);

        stockTransferRepository.save(StockTransfer.builder()
                .sourceWarehouseId(request.getSourceWarehouseId())
                .targetWarehouseId(request.getTargetWarehouseId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .transferDate(LocalDateTime.now())
                .build());
    }

    @Override
    public Page<Allocation> searchAllocations(Long pId, Long wId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        LocalDateTime minDate = (start == null) ? LocalDateTime.now().minusYears(1) : start;
        LocalDateTime maxDate = (end == null) ? LocalDateTime.now() : end;
        return allocationRepository.searchAllocations(pId, wId, minDate, maxDate, pageable);
    }
}