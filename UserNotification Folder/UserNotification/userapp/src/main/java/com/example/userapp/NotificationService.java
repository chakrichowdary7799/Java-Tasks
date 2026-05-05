package com.example.userapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationHistoryRepository historyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationPreferenceService preferenceService;

    // Send notification
    public NotificationHistory sendNotification(Long userId, String channel, String message) {
        
        // check user exists
        User user = userService.getUserById(userId);

        // create history record
        NotificationHistory history = new NotificationHistory();
        history.setUser(user);
        history.setChannel(channel.toUpperCase());
        history.setMessage(message);
        history.setSentAt(LocalDateTime.now());

        // check if channel is enabled
        boolean isEnabled = preferenceService.isChannelEnabled(userId, channel);

        if (isEnabled) {
            history.setStatus("SENT");
        } else {
            history.setStatus("FAILED");
        }

        return historyRepository.save(history);
    }

    // Get all notifications for a user
    public List<NotificationHistory> getAllNotifications(Long userId) {
        userService.getUserById(userId);
        return historyRepository.findByUserId(userId);
    }

    // Get notifications by status
    public List<NotificationHistory> getByStatus(Long userId, String status) {
        userService.getUserById(userId);
        return historyRepository.findByUserIdAndStatus(userId, status.toUpperCase());
    }

    // Get notifications by channel
    public List<NotificationHistory> getByChannel(Long userId, String channel) {
        userService.getUserById(userId);
        return historyRepository.findByUserIdAndChannel(userId, channel.toUpperCase());
    }
}