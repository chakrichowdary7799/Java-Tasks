package com.enterprise.drs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enterprise.drs.model.RestoreLog;

public interface RestoreLogRepository extends JpaRepository<RestoreLog, String> {
}