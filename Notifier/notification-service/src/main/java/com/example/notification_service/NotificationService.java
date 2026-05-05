package com.example.notification_service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired private UserRepository userRepo;
    @Autowired private HistoryRepository historyRepo;

    public void triggerNotification(@NonNull Long userId, String type, String message) {
        User user = userRepo.findById(userId).orElseThrow();
        Preference pref = user.getPreference();
        
        boolean isEnabled = switch (type.toUpperCase()) {
            case "EMAIL" -> pref.isEmailEnabled();
            case "SMS" -> pref.isSmsEnabled();
            case "PUSH" -> pref.isPushEnabled();
            default -> false;
        };

        NotificationHistory log = new NotificationHistory();
        log.setUserId(userId);
        log.setMessage(message);
        log.setType(type);

        if (isEnabled) {
            log.setStatus("SENT");
        } else {
            log.setStatus("FAILED");
        }
        historyRepo.save(log);
    }

    public List<NotificationHistory> getHistoryByUser(Long userId) {
        throw new UnsupportedOperationException("Unimplemented method 'getHistoryByUser'");
    }
}
