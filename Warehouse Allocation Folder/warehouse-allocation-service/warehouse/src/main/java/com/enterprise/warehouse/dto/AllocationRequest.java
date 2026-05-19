package com.enterprise.warehouse.dto;

import lombok.Data;

@Data
public class AllocationRequest {
    private Long productId;
    private Integer quantity;
}