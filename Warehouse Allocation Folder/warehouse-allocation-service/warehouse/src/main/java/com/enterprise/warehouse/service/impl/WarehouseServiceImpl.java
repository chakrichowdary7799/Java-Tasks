package com.enterprise.warehouse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enterprise.warehouse.exception.ResourceNotFoundException;
import com.enterprise.warehouse.model.Warehouse;
import com.enterprise.warehouse.repository.WarehouseRepository;
import com.enterprise.warehouse.service.WarehouseService;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired private WarehouseRepository warehouseRepository;

    @Override
    public Warehouse createWarehouse(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse updateWarehouse(Long id, Warehouse updated) {
        Warehouse existing = warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
        existing.setName(updated.getName());
        existing.setLocation(updated.getLocation());
        existing.setCapacity(updated.getCapacity());
        return warehouseRepository.save(existing);
    }

    @Override
    public void deleteWarehouse(Long id) {
        if(!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse not found");
        }
        warehouseRepository.deleteById(id);
    }

    @Override
    public List<Warehouse> getAllActiveWarehouses() {
        return warehouseRepository.findAll();
    }
}