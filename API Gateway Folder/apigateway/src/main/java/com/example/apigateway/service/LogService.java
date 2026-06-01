package com.example.apigateway.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.apigateway.entity.RequestLog;
import com.example.apigateway.repository.RequestLogRepository;

@Service
public class LogService {

    private final RequestLogRepository logRepository;

    public LogService(RequestLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog(RequestLog log) {
        logRepository.save(log);
    }

    public List<RequestLog> getAllLogs() {
        return logRepository.findAll();
    }
}