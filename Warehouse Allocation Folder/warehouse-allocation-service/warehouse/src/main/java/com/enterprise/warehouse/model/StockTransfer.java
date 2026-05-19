package com.enterprise.warehouse.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_transfer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "source_warehouse_id")
    private Long sourceWarehouseId;
    
    @Column(name = "target_warehouse_id")
    private Long targetWarehouseId;
    
    @Column(name = "product_id")
    private Long productId;
    
    private Integer quantity;
    
    @Column(name = "transfer_date")
    private LocalDateTime transferDate;
}