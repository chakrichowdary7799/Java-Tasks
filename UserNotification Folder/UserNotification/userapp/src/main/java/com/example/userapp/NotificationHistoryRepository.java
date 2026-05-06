package com.example.userapp;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationHistoryRepository 
    extends JpaRepository<NotificationHistory, Long> {

    // Get all notifications for a user
    List<NotificationHistory> findByUserId(Long userId);

    // Get notifications by status (SENT or FAILED)
    List<NotificationHistory> findByUserIdAndStatus(Long userId, String status);

    // Get notifications by channel (EMAIL, SMS, PUSH)
    List<NotificationHistory> findByUserIdAndChannel(Long userId, String channel);
}