package com.enterprise.drs.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DatabaseBackupService {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public void exportDatabase(String targetRawSqlPath) throws IOException, InterruptedException {
        String dbName = extractDatabaseName(dbUrl);
        ProcessBuilder pb = new ProcessBuilder(
                "mysqldump",
                "-u" + username,
                "-p" + password,
                "--databases", dbName,
                "-r", targetRawSqlPath
        );
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("mysqldump failed execution routine with system exit code: " + exitCode);
        }
    }

    public void importDatabase(String sourceRawSqlPath) throws IOException, InterruptedException {
        String dbName = extractDatabaseName(dbUrl);
        ProcessBuilder pb = new ProcessBuilder(
                "mysql",
                "-u" + username,
                "-p" + password,
                dbName,
                "-e", "source " + sourceRawSqlPath
        );
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("mysql restoration execution routine failed with system exit code: " + exitCode);
        }
    }

    private String extractDatabaseName(String url) {
        String cleanUrl = url.split("\\?")[0];
        return cleanUrl.substring(cleanUrl.lastIndexOf("/") + 1);
    }
}