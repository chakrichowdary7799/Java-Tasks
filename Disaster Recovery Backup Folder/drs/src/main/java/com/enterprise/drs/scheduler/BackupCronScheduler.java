package com.enterprise.drs.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.enterprise.drs.model.BackupMetadata;
import com.enterprise.drs.service.BackupService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BackupCronScheduler {
    private final BackupService backupService;

    // Automatically executes an Enterprise Full Sync Backup sequence every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void executeScheduledMidnightBackupRoutine() {
        String mockSourceDir = "./storage/production-file-assets";
        BackupMetadata meta = backupService.initiateBackup("FULL");
        backupService.runAsynchronousBackupPipeline(meta.getId(), mockSourceDir);
    }
}