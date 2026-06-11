package com.enterprise.drs.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.enterprise.drs.util.ChecksumUtil;

@Service
public class ValidationService {
    @SuppressWarnings("UseSpecificCatch")
    public boolean validateIntegrity(String filePath, String expectedChecksum) {
        try {
            File file = new File(filePath);
            if (!file.exists()) return false;
            String actualChecksum = ChecksumUtil.calculateMD5(filePath);
            return actualChecksum.equalsIgnoreCase(expectedChecksum);
        } catch (Exception e) {
            return false;
        }
    }
}