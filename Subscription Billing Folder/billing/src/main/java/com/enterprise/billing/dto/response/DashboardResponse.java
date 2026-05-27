package com.enterprise.billing.dto.response;

public class DashboardResponse {
    private long totalUsers;
    private long activeSubscriptions;
    private double totalRevenue;

    public DashboardResponse(long totalUsers, long activeSubscriptions, double totalRevenue) {
        this.totalUsers = totalUsers;
        this.activeSubscriptions = activeSubscriptions;
        this.totalRevenue = totalRevenue;
    }

    // Getters and Setters
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getActiveSubscriptions() { return activeSubscriptions; }
    public void setActiveSubscriptions(long activeSubscriptions) { this.activeSubscriptions = activeSubscriptions; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
}