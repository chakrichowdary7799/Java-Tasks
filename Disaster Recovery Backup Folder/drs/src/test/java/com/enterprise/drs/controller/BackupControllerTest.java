package com.enterprise.drs.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.enterprise.drs.model.BackupMetadata;
import com.enterprise.drs.repository.BackupMetadataRepository;
import com.enterprise.drs.service.BackupService;

@WebMvcTest(BackupController.class)
public class BackupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BackupService backupService;

    @MockBean
    private BackupMetadataRepository repository;

    @Test
    public void testStartBackupEndpointReturns200AndValidPayload() throws Exception {
        BackupMetadata mockMeta = BackupMetadata.builder()
                .id("mock-id-123")
                .type("FULL")
                .timestamp(LocalDateTime.now())
                .status("PENDING")
                .location("PENDING_ALLOCATION")
                .build();

        // Stubs out the internal business engine framework execution logic layers
        when(backupService.initiateBackup(anyString())).thenReturn(mockMeta);

        String jsonPayload = """
                {
                    "type": "FULL",
                    "sourceDirectory": "./storage/test-assets"
                }
                """;

        mockMvc.perform(post("/backup/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("mock-id-123"))
                .andExpect(jsonPath("$.type").value("FULL"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testGetBackupStatusEndpointReturnsValidRecordData() throws Exception {
        BackupMetadata mockMeta = BackupMetadata.builder()
                .id("mock-id-123")
                .type("FULL")
                .timestamp(LocalDateTime.now())
                .status("COMPLETED")
                .location("/storage/backups/archive.gz")
                .checksum("abc123checksum")
                .compressedSizeBytes(5000L)
                .build();

        when(repository.findById("mock-id-123")).thenReturn(Optional.of(mockMeta));

        mockMvc.perform(get("/backup/status/mock-id-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("mock-id-123"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.checksum").value("abc123checksum"));
    }
}