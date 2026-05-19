package com.enterprise.warehouse.service;

import java.util.List;

import com.enterprise.warehouse.model.Warehouse;

public interface WarehouseService {
    Warehouse createWarehouse(Warehouse warehouse);
    Warehouse updateWarehouse(Long id, Warehouse warehouse);
    void deleteWarehouse(Long id);
    List<Warehouse> getAllActiveWarehouses();
}