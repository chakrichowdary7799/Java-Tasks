package com.enterprise.warehouse.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.warehouse.dto.AllocationRequest;
import com.enterprise.warehouse.dto.AllocationResponse;
import com.enterprise.warehouse.dto.StockTransferRequest;
import com.enterprise.warehouse.model.Allocation;
import com.enterprise.warehouse.service.AllocationService;

@RestController
@RequestMapping("/api/allocations")
public class AllocationController {
    @Autowired private AllocationService allocationService;

    @PostMapping
    public ResponseEntity<AllocationResponse> allocate(@RequestBody AllocationRequest request) {
        return ResponseEntity.ok(allocationService.allocateStock(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody StockTransferRequest request) {
        allocationService.transferStock(request);
        return ResponseEntity.ok("Stock transfer processed successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Allocation>> search(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "allocatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(allocationService.searchAllocations(productId, warehouseId, startDate, endDate, pageable));
    }
}