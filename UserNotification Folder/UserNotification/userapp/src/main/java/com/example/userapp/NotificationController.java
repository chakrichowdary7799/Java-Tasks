package com.example.userapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Send notification
    @PostMapping("/send")//http://localhost:8080/api/notifications/send?userId=1&channel=email&message=Hello
    public ResponseEntity<NotificationHistory> sendNotification(
            @RequestParam Long userId,
            @RequestParam String channel,
            @RequestParam String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.sendNotification(userId, channel, message));
    }

    // Get all notifications for a user
    @GetMapping("/user/{userId}")//http://localhost:8080/api/notifications/user/1
    public ResponseEntity<List<NotificationHistory>> getAllNotifications(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
            notificationService.getAllNotifications(userId));
    }

    // Get notifications by status
    @GetMapping("/user/{userId}/status/{status}")//http://localhost:8080/api/notifications/user/1/status/sent
    public ResponseEntity<List<NotificationHistory>> getByStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        return ResponseEntity.ok(
            notificationService.getByStatus(userId, status));
    }

    // Get notifications by channel
    @GetMapping("/user/{userId}/channel/{channel}")//http://localhost:8080/api/notifications/user/1/channel/email
    public ResponseEntity<List<NotificationHistory>> getByChannel(
            @PathVariable Long userId,
            @PathVariable String channel) {
        return ResponseEntity.ok(
            notificationService.getByChannel(userId, channel));
    }
}
