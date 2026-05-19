package com.enterprise.warehouse.service;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enterprise.warehouse.dto.AllocationRequest;
import com.enterprise.warehouse.dto.AllocationResponse;
import com.enterprise.warehouse.exception.InsufficientStockException;
import com.enterprise.warehouse.model.Product;
import com.enterprise.warehouse.model.WarehouseInventory;
import com.enterprise.warehouse.repository.AllocationRepository;
import com.enterprise.warehouse.repository.ProductRepository;
import com.enterprise.warehouse.repository.WarehouseInventoryRepository;
import com.enterprise.warehouse.service.impl.AllocationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AllocationServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private WarehouseInventoryRepository inventoryRepository;
    @Mock private AllocationRepository allocationRepository;

    @InjectMocks
    private AllocationServiceImpl allocationService;

    private AllocationRequest request;
    private Product product;
    private WarehouseInventory inventory;

    @BeforeEach
    void setUp() {
        request = new AllocationRequest();
        request.setProductId(1L);
        request.setQuantity(10);

        product = new Product(1L, "Test Product", "SKU-123", 100);
        inventory = new WarehouseInventory(1L, 1L, 1L, 50, 0L);
    }

    @Test
    void testAllocateStock_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductIdOrderByAvailableQuantityDesc(1L))
                .thenReturn(Collections.singletonList(inventory));

        AllocationResponse response = allocationService.allocateStock(request);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(40, inventory.getAvailableQuantity());
        verify(inventoryRepository, times(1)).save(inventory);
        verify(allocationRepository, times(1)).save(any());
    }

    @Test
    void testAllocateStock_InsufficientStock() {
        request.setQuantity(100);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductIdOrderByAvailableQuantityDesc(1L))
                .thenReturn(Collections.singletonList(inventory));

        assertThrows(InsufficientStockException.class, () -> allocationService.allocateStock(request));
        verify(inventoryRepository, never()).save(any());
    }
}