package com.example.notification_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {
    @Autowired private UserService userService;
    @Autowired private NotificationService notifyService;

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/users/{id}/preferences")
    public Preference updatePrefs(@PathVariable @NonNull Long id, @RequestBody Preference prefs) {
        return userService.updatePreferences(id, prefs);
    }

    @PostMapping("/notify")
    public ResponseEntity<String> send(@RequestParam @NonNull Long userId, @RequestParam String type, @RequestParam String msg) {
        notifyService.triggerNotification(userId, type, msg);
        return ResponseEntity.ok("Attempted notification recorded.");
    }

    @GetMapping("/history/{userId}")
    public List<NotificationHistory> getHistory(@PathVariable Long userId) {
        return notifyService.getHistoryByUser(userId);
    }
}
