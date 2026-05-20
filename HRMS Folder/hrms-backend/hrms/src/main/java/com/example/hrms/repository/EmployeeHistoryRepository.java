package com.example.hrms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hrms.model.EmployeeHistoryLog;

@Repository
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistoryLog, Long> {
}