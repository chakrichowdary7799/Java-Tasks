package com.enterprise.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "warehouse_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "warehouse_id")
    private Long warehouseId;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "available_quantity")
    private Integer availableQuantity;
    
    @Version
    private Long version;
}