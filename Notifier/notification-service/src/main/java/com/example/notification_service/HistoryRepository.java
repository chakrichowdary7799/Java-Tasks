package com.example.notification_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    public default void save(NotificationHistory log) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

}
