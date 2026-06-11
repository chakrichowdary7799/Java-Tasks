package com.enterprise.drs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BackupRequest {
    @NotBlank(message = "Backup configuration processing strategy type designation context is parameter mandatory.")
    private String type; // FULL, INCREMENTAL, DIFFERENTIAL

    @NotBlank(message = "Source directory targeting reference cannot remain unassigned or blank parameters values.")
    private String sourceDirectory;
}