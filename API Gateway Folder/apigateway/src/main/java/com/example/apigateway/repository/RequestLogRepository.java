package com.example.apigateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apigateway.entity.RequestLog;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
}