package com.enterprise.drs.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BackupStatusResponse {
    private String id;
    private String type;
    private LocalDateTime timestamp;
    private String location;
    private String status;
    private String checksum;
    private Long sizeBytes;
}