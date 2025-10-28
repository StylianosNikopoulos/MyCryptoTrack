package com.mycryptotrack.alert.repository;

import com.mycryptotrack.alert.entity.NotificationData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationData, Long> {
    List<NotificationData> findByEmailOrderByCreatedAtDesc(String email);
}
