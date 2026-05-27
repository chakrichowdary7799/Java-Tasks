package com.enterprise.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.enterprise.billing.entity.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(s.price), 0.0) FROM Subscription s WHERE s.status = 'ACTIVE'")
    double sumPriceByStatusActive();
}