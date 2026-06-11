package com.enterprise.drs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestoreRequest {
    @NotBlank(message = "Target trace structural record identification snapshot metadata target mapping locator key required.")
    private String backupId;

    @NotBlank(message = "Target execution structural context engine database sql definition script path routing string reference required.")
    private String databaseTargetScriptPath;
}