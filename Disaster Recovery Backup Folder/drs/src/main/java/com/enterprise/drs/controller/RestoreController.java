package com.enterprise.drs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.drs.dto.RestoreRequest;
import com.enterprise.drs.dto.RestoreStatusResponse;
import com.enterprise.drs.model.RestoreLog;
import com.enterprise.drs.repository.RestoreLogRepository;
import com.enterprise.drs.service.RestoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restore")
@RequiredArgsConstructor
public class RestoreController {
    private final RestoreService restoreService;
    private final RestoreLogRepository repository;

    @PostMapping
    public ResponseEntity<RestoreStatusResponse> startRestore(@Valid @RequestBody RestoreRequest request) {
        RestoreLog log = restoreService.initiateRestore(request.getBackupId());
        restoreService.runAsynchronousRestorePipeline(log.getId(), request.getDatabaseTargetScriptPath());

        return ResponseEntity.ok(RestoreStatusResponse.builder()
                .id(log.getId())
                .backupId(log.getBackupMetadata().getId())
                .status(log.getStatus())
                .executionTime(log.getTime())
                .build());
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<RestoreStatusResponse> getRestoreStatus(@PathVariable String id) {
        RestoreLog log = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restore tracking trace log metrics transaction target reference mapping index not found."));
        
        return ResponseEntity.ok(RestoreStatusResponse.builder()
                .id(log.getId())
                .backupId(log.getBackupMetadata().getId())
                .status(log.getStatus())
                .executionTime(log.getTime())
                .failureDetails(log.getFailureReason())
                .build());
    }
}