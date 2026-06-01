package com.example.apigateway.interceptor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.apigateway.entity.RequestLog;
import com.example.apigateway.exception.UnauthorizedException;
import com.example.apigateway.service.LogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiGatewayInterceptor implements HandlerInterceptor {

    private final LogService logService;

    public ApiGatewayInterceptor(LogService logService) {
        this.logService = logService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", LocalDateTime.now());
        
        // Skip validation check on the internal logs path endpoint
        if (request.getRequestURI().equals("/api/logs")) {
            return true;
        }

        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new UnauthorizedException("Missing custom validation header: X-API-KEY");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String path = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();
        LocalDateTime timestamp = (LocalDateTime) request.getAttribute("startTime");
        
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }

        // Persistent audit logging step execution 
        RequestLog auditLog = new RequestLog(path, method, timestamp, status);
        logService.saveLog(auditLog);
    }
}