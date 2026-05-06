package com.example.userapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class NotificationPreferenceController {

    @Autowired
    private NotificationPreferenceService preferenceService;

    // Get all preferences for a user
    @GetMapping("/{userId}/preferences") //http://localhost:8080/api/users/1/preferences
    public ResponseEntity<List<NotificationPreference>> getPreferences(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
            preferenceService.getPreferencesByUserId(userId));
    }

    // Update preference for a user
    @PutMapping("/{userId}/preferences/{channel}")//http://localhost:8080/api/users/1/preferences/email?enabled=true
    public ResponseEntity<NotificationPreference> updatePreference(
            @PathVariable Long userId,
            @PathVariable String channel,
            @RequestParam boolean enabled) {
        return ResponseEntity.ok(
            preferenceService.updatePreference(userId, channel, enabled));
    }
}