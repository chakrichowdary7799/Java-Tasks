package com.example.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class InventoryEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused") // Explicitly clears the VS Code field analysis warning
    private Long id;
    
    private String productSku;
    private Integer availableStock;

    public InventoryEntity() {}

    public InventoryEntity(Long id, String productSku, Integer availableStock) {
        this.id = id;
        this.productSku = productSku;
        this.availableStock = availableStock;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }

    public Integer getAvailableStock() { return availableStock; }
    public void setAvailableStock(Integer availableStock) { this.availableStock = availableStock; }
}
