package com.enterprise.billing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.billing.dto.response.DashboardResponse;
import com.enterprise.billing.repository.SubscriptionRepository;
import com.enterprise.billing.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard Metrics Panel", description = "Administrative endpoints displaying live statistics")
public class DashboardController {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public DashboardController(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @GetMapping("/metrics")
    @Operation(summary = "Return total count registers, active accounts and revenue values")
    public DashboardResponse getMetrics() {
        long totalUsers = userRepository.count();
        long activeSubs = subscriptionRepository.countByStatus("ACTIVE");
        double activeRevenue = subscriptionRepository.sumPriceByStatusActive();
        
        return new DashboardResponse(totalUsers, activeSubs, activeRevenue);
    }
}