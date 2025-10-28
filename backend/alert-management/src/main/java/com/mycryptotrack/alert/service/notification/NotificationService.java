package com.mycryptotrack.alert.service.notification;

import com.mycryptotrack.alert.dto.NotificationDataDto;
import java.util.List;

public interface NotificationService {
    List<NotificationDataDto> getAllNotifications();
    NotificationDataDto createNotification(String email, String message);
    void deleteNotification(Long id);
    NotificationDataDto markAsRead(Long id);
}
