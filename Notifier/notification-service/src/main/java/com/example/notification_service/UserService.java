package com.example.notification_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        if (user.getPreference() != null) {
            user.getPreference().setUser(user);
        }
        return userRepository.save(user);
    }

    @Transactional
    public Preference updatePreferences(@NonNull Long userId, Preference prefs) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Preference existing = user.getPreference();
        if (existing == null) {
            prefs.setUser(user);
            user.setPreference(prefs);
            return userRepository.save(user).getPreference();
        }

        existing.setEmailEnabled(prefs.isEmailEnabled());
        existing.setSmsEnabled(prefs.isSmsEnabled());
        existing.setPushEnabled(prefs.isPushEnabled());

        user.setPreference(existing);
        return userRepository.save(user).getPreference();
    }
}
