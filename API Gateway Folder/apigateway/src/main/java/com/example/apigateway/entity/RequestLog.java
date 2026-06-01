package com.example.apigateway.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "request_logs")
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_path", nullable = false)
    private String requestPath;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private int status;

    public RequestLog() {}

    public RequestLog(String requestPath, String method, LocalDateTime timestamp, int status) {
        this.requestPath = requestPath;
        this.method = method;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRequestPath() { return requestPath; }
    public void setRequestPath(String requestPath) { this.requestPath = requestPath; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}