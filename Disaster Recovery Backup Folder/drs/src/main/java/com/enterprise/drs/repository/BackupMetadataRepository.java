package com.enterprise.drs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enterprise.drs.model.BackupMetadata;


public interface BackupMetadataRepository extends JpaRepository<BackupMetadata, String> {
}