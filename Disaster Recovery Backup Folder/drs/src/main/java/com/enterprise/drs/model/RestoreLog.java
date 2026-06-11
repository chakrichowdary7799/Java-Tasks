package com.enterprise.drs.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restore_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestoreLog {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_id", nullable = false)
    private BackupMetadata backupMetadata;

    @Column(nullable = false, length = 20)
    private String status; // IN_PROGRESS, SUCCESS, FAILED

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;
}