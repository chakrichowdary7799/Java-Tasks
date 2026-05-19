package com.enterprise.warehouse.dto;

import lombok.Data;

@Data
public class StockTransferRequest {
    private Long sourceWarehouseId;
    private Long targetWarehouseId;
    private Long productId;
    private Integer quantity;
}