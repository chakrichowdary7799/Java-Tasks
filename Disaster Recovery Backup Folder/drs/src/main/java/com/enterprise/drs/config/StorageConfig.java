package com.enterprise.drs.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class StorageConfig {
    @Value("${app.backup.local-storage-path:./storage/backups}")
    private String localStoragePath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(localStoragePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize local backup storage directory structure", e);
        }
    }

    public String getLocalStoragePath() {
        return localStoragePath;
    }
}