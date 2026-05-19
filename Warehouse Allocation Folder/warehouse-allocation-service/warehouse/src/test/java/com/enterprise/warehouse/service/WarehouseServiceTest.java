package com.enterprise.warehouse.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enterprise.warehouse.exception.ResourceNotFoundException;
import com.enterprise.warehouse.model.Warehouse;
import com.enterprise.warehouse.model.WarehouseStatus;
import com.enterprise.warehouse.repository.WarehouseRepository;
import com.enterprise.warehouse.service.impl.WarehouseServiceImpl;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {

    @Mock 
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse(1L, "Central Hub Alpha", "Chicago", 50000.0, WarehouseStatus.ACTIVE, LocalDateTime.now());
    }

    @Test
    void testCreateWarehouse_Success() {
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        Warehouse result = warehouseService.createWarehouse(warehouse);

        assertNotNull(result);
        assertEquals("Central Hub Alpha", result.getName());
        verify(warehouseRepository, times(1)).save(warehouse);
    }

    @Test
    void testUpdateWarehouse_Success() {
        Warehouse updatedInfo = new Warehouse();
        updatedInfo.setName("Updated Hub Name");
        updatedInfo.setLocation("New York");
        updatedInfo.setCapacity(60000.0);

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        Warehouse result = warehouseService.updateWarehouse(1L, updatedInfo);

        assertNotNull(result);
        assertEquals("Updated Hub Name", result.getName());
        assertEquals("New York", result.getLocation());
        verify(warehouseRepository, times(1)).findById(1L);
        verify(warehouseRepository, times(1)).save(warehouse);
    }

    @Test
    void testUpdateWarehouse_NotFound() {
        Warehouse updatedInfo = new Warehouse();
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> warehouseService.updateWarehouse(1L, updatedInfo));
        verify(warehouseRepository, never()).save(any());
    }

    @Test
    void testDeleteWarehouse_Success() {
        when(warehouseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(warehouseRepository).deleteById(1L);

        assertDoesNotThrow(() -> warehouseService.deleteWarehouse(1L));

        verify(warehouseRepository, times(1)).existsById(1L);
        verify(warehouseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteWarehouse_NotFound() {
        when(warehouseRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> warehouseService.deleteWarehouse(1L));
        verify(warehouseRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetAllActiveWarehouses_Success() {
        when(warehouseRepository.findAll()).thenReturn(Collections.singletonList(warehouse));

        List<Warehouse> activeList = warehouseService.getAllActiveWarehouses();

        assertNotNull(activeList);
        assertEquals(1, activeList.size());
        assertEquals(WarehouseStatus.ACTIVE, activeList.get(0).getStatus());
        verify(warehouseRepository, times(1)).findAll();
    }
}