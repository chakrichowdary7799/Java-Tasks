package com.enterprise.warehouse.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllocationResponse {
    private Long allocationId;
    private Long productId;
    private Long warehouseId;
    private Integer quantity;
    private String status;
    private LocalDateTime timestamp;
}