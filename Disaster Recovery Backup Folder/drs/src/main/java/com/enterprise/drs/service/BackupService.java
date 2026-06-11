package com.enterprise.drs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.enterprise.drs.config.StorageConfig;
import com.enterprise.drs.model.BackupMetadata;
import com.enterprise.drs.repository.BackupMetadataRepository;
import com.enterprise.drs.util.ChecksumUtil;
import com.enterprise.drs.util.CompressionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BackupService {
    private final BackupMetadataRepository metadataRepository;
    private final DatabaseBackupService databaseBackupService;
    private final FileStorageBackupService fileStorageBackupService;
    private final StorageConfig storageConfig;

    public BackupMetadata initiateBackup(String type) {
        String backupId = UUID.randomUUID().toString();
        BackupMetadata meta = BackupMetadata.builder()
                .id(backupId)
                .type(type.toUpperCase())
                .timestamp(LocalDateTime.now())
                .location("PENDING_ALLOCATION")
                .status("PENDING")
                .build();
        return metadataRepository.save(meta);
    }

    @Async("taskExecutor")
    @SuppressWarnings("UseSpecificCatch")
    public void runAsynchronousBackupPipeline(String backupId, String fileSourceDir) {
        BackupMetadata meta = metadataRepository.findById(backupId).orElse(null);
        if (meta == null) return;

        meta.setStatus("IN_PROGRESS");
        metadataRepository.save(meta);

        try {
            String baseDir = storageConfig.getLocalStoragePath();
            String rawSqlPath = baseDir + File.separator + backupId + "_db.sql";
            String rawFilesZipPath = baseDir + File.separator + backupId + "_files.zip";
            String combinedTarPath = baseDir + File.separator + backupId + "_combined.raw";
            String finalCompressedPath = baseDir + File.separator + backupId + "_archive.gz";

            // 1. Dump DB
            databaseBackupService.exportDatabase(rawSqlPath);
            
            // 2. Dump file system store
            fileStorageBackupService.backupDirectory(fileSourceDir, rawFilesZipPath);

            // 3. Combined logical mapping encapsulation inside a raw storage directory
            File rawSql = new File(rawSqlPath);
            File rawFiles = new File(rawFilesZipPath);
            File combined = new File(combinedTarPath);
            
            // Simple byte aggregation file stitching for multi-part bundle
            try (FileOutputStream fos = new FileOutputStream(combined);
                 FileInputStream fisSql = new FileInputStream(rawSql);
                 FileInputStream fisFiles = new FileInputStream(rawFiles)) {
                 fisSql.transferTo(fos);
                 fisFiles.transferTo(fos);
            }

            // 4. Compress unified asset output
            File compressedArchive = new File(finalCompressedPath);
            CompressionUtil.compressFile(combined, compressedArchive);

            // 5. Generate secure checksum checks and record baseline footprint specifications
            String checksum = ChecksumUtil.calculateMD5(finalCompressedPath);
            long bytesize = compressedArchive.length();

            // Cleanup temp artifacts
            rawSql.delete();
            rawFiles.delete();
            combined.delete();

            meta.setStatus("COMPLETED");
            meta.setLocation(compressedArchive.getAbsolutePath());
            meta.setChecksum(checksum);
            meta.setCompressedSizeBytes(bytesize);
            metadataRepository.save(meta);

        } catch (Exception e) {
            meta.setStatus("FAILED");
            metadataRepository.save(meta);
        }
    }
}