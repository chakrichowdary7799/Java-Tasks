package com.example.userapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationPreferenceService {

    @Autowired
    private NotificationPreferenceRepository preferenceRepository;

    @Autowired
    private UserService userService;

    // Get all preferences for a user
    public List<NotificationPreference> getPreferencesByUserId(Long userId) {
        userService.getUserById(userId); // check user exists
        return preferenceRepository.findByUserId(userId);
    }

    // Enable or disable a channel for a user
    public NotificationPreference updatePreference(Long userId, String channel, boolean enabled) {
        userService.getUserById(userId); // check user exists

        NotificationPreference preference = preferenceRepository
            .findByUserIdAndChannel(userId, channel)
            .orElse(new NotificationPreference());

        preference.setUser(userService.getUserById(userId));
        preference.setChannel(channel.toUpperCase());
        preference.setEnabled(enabled);

        return preferenceRepository.save(preference);
    }

    // Check if channel is enabled for user
    public boolean isChannelEnabled(Long userId, String channel) {
        return preferenceRepository
            .findByUserIdAndChannel(userId, channel.toUpperCase())
            .map(NotificationPreference::isEnabled)
            .orElse(false);
    }
}