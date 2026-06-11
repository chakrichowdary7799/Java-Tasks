package com.enterprise.drs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.drs.dto.BackupRequest;
import com.enterprise.drs.dto.BackupStatusResponse;
import com.enterprise.drs.model.BackupMetadata;
import com.enterprise.drs.repository.BackupMetadataRepository;
import com.enterprise.drs.service.BackupService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/backup")
@RequiredArgsConstructor
public class BackupController {
    private final BackupService backupService;
    private final BackupMetadataRepository repository;

    @PostMapping("/start")
    public ResponseEntity<BackupStatusResponse> startBackup(@Valid @RequestBody BackupRequest request) {
        BackupMetadata meta = backupService.initiateBackup(request.getType());
        backupService.runAsynchronousBackupPipeline(meta.getId(), request.getSourceDirectory());
        
        return ResponseEntity.ok(BackupStatusResponse.builder()
                .id(meta.getId())
                .type(meta.getType())
                .timestamp(meta.getTimestamp())
                .status(meta.getStatus())
                .location(meta.getLocation())
                .build());
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<BackupStatusResponse> getBackupStatus(@PathVariable String id) {
        BackupMetadata meta = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Backup record execution snapshot trace context not located."));
        
        return ResponseEntity.ok(BackupStatusResponse.builder()
                .id(meta.getId())
                .type(meta.getType())
                .timestamp(meta.getTimestamp())
                .status(meta.getStatus())
                .location(meta.getLocation())
                .checksum(meta.getChecksum())
                .sizeBytes(meta.getCompressedSizeBytes())
                .build());
    }
}