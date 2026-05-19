package com.enterprise.warehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.warehouse.model.Warehouse;
import com.enterprise.warehouse.service.WarehouseService;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {
    @Autowired private WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<Warehouse> create(@RequestBody Warehouse w) {
        return ResponseEntity.ok(warehouseService.createWarehouse(w));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> update(@PathVariable Long id, @RequestBody Warehouse w) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, w));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Warehouse>> list() {
        return ResponseEntity.ok(warehouseService.getAllActiveWarehouses());
    }
}