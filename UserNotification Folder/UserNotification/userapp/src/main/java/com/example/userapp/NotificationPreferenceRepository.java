package com.example.userapp;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NotificationPreferenceRepository 
    extends JpaRepository<NotificationPreference, Long> {

    // Get all preferences for a user
    List<NotificationPreference> findByUserId(Long userId);

    // Get specific channel preference for a user
    Optional<NotificationPreference> findByUserIdAndChannel(Long userId, String channel);
}