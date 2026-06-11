package com.enterprise.drs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.enterprise.drs.config.StorageConfig;
import com.enterprise.drs.model.BackupMetadata;
import com.enterprise.drs.repository.BackupMetadataRepository;

public class BackupServiceTest {

    @InjectMocks
    private BackupService backupService;

    @Mock
    private BackupMetadataRepository metadataRepository;

    @Mock
    @SuppressWarnings("unused")
    private DatabaseBackupService databaseBackupService;

    @Mock
    @SuppressWarnings("unused")
    private FileStorageBackupService fileStorageBackupService;

    @Mock
    @SuppressWarnings("unused")
    private StorageConfig storageConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitiateBackupSavesMetadataCorrectly() {
        BackupMetadata metaMock = BackupMetadata.builder()
                .id("test-uuid")
                .type("FULL")
                .status("PENDING")
                .build();

        when(metadataRepository.save(any(BackupMetadata.class))).thenReturn(metaMock);

        BackupMetadata returnedMeta = backupService.initiateBackup("FULL");

        assertNotNull(returnedMeta);
        assertEquals("FULL", returnedMeta.getType());
        assertEquals("PENDING", returnedMeta.getStatus());
        verify(metadataRepository, times(1)).save(any(BackupMetadata.class));
    }
}