package com.enterprise.security.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("deprecation")
    public void blacklistToken(String token, long expirationTimeMs) {
        try {
            redisTemplate.opsForValue().set(token, "blacklisted", expirationTimeMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.err.println("Redis is offline. Skipping token blacklisting: " + e.getMessage());
        }
    }

    public boolean isTokenBlacklisted(String token) {
        if (token == null || token.isEmpty()) return false;
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(token));
        } catch (Exception e) {
            System.err.println("Redis connection error. Bypassing blacklist check: " + e.getMessage());
            return false; // Fail-safe: allows app to run even if Redis drops out
        }
    }
}
