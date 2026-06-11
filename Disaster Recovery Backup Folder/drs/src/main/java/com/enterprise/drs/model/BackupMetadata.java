package com.enterprise.drs.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "backup_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupMetadata {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 20)
    private String type; // FULL, INCREMENTAL, DIFFERENTIAL

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(nullable = false, length = 20)
    private String status; // PENDING, IN_PROGRESS, COMPLETED, FAILED

    @Column(length = 64)
    private String checksum;

    @Column(name = "compressed_size_bytes")
    private Long compressedSizeBytes;
}