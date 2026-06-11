package com.enterprise.drs.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.enterprise.drs.config.StorageConfig;
import com.enterprise.drs.model.BackupMetadata;
import com.enterprise.drs.model.RestoreLog;
import com.enterprise.drs.repository.BackupMetadataRepository;
import com.enterprise.drs.repository.RestoreLogRepository;
import com.enterprise.drs.util.CompressionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestoreService {
    private final BackupMetadataRepository metadataRepository;
    private final RestoreLogRepository restoreLogRepository;
    private final ValidationService validationService;
    private final DatabaseBackupService databaseBackupService;
    private final StorageConfig storageConfig;

    public RestoreLog initiateRestore(String backupId) {
        BackupMetadata meta = metadataRepository.findById(backupId)
                .orElseThrow(() -> new IllegalArgumentException("Target backup image sequence index hash parameter key not found."));

        String restoreId = UUID.randomUUID().toString();
        RestoreLog log = RestoreLog.builder()
                .id(restoreId)
                .backupMetadata(meta)
                .status("IN_PROGRESS")
                .time(LocalDateTime.now())
                .build();
        return restoreLogRepository.save(log);
    }

    @Async("taskExecutor")
    @SuppressWarnings("UseSpecificCatch")
    public void runAsynchronousRestorePipeline(String restoreId, String targetSqlRestorePath) {
        RestoreLog log = restoreLogRepository.findById(restoreId).orElse(null);
        if (log == null) return;

        BackupMetadata meta = log.getBackupMetadata();
        
        // 1. Structural checksum trace pattern matching verification check
        if (!validationService.validateIntegrity(meta.getLocation(), meta.getChecksum())) {
            log.setStatus("FAILED");
            log.setFailureReason("Checksum verification failure. Compromised, altered or missing archival assets.");
            restoreLogRepository.save(log);
            return;
        }

        try {
            String baseDir = storageConfig.getLocalStoragePath();
            String decompressedPath = baseDir + File.separator + meta.getId() + "_restored.raw";
            
            // Decompress file engine sequence runtime mapping process 
            CompressionUtil.decompressFile(new File(meta.getLocation()), new File(decompressedPath));

            // Executing underlying native transactional context load schemas block structure pipeline hooks
            databaseBackupService.importDatabase(targetSqlRestorePath);

            new File(decompressedPath).delete();

            log.setStatus("SUCCESS");
            restoreLogRepository.save(log);
        } catch (Exception e) {
            log.setStatus("FAILED");
            log.setFailureReason(e.getMessage());
            restoreLogRepository.save(log);
        }
    }
}