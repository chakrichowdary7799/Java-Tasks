package com.enterprise.warehouse.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditLogAspect {
    private static final Logger log = LoggerFactory.getLogger(AuditLogAspect.class);

    @Around("execution(* com.enterprise.warehouse.service..*(..))")
    public Object auditPerformanceAndLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        
        log.info("[AUDIT START] Executing business transaction: {}", methodName);
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            
            log.info("[AUDIT SUCCESS] {} completed in {}ms", methodName, duration);
            if (duration > 500) {
                log.warn("[PERFORMANCE WARNING] NFR Breached! {} execution took more than 500ms limit.", methodName);
            }
            return result;
        } catch (Throwable ex) {
            log.error("[AUDIT FAILURE] Method {} threw exception: {}", methodName, ex.getMessage());
            throw ex;
        }
    }
}