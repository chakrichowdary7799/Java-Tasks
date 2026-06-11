package com.enterprise.drs.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestoreStatusResponse {
    private String id;
    private String backupId;
    private String status;
    private LocalDateTime executionTime;
    private String failureDetails;
}