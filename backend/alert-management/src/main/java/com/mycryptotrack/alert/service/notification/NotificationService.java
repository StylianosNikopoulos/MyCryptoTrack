package com.mycryptotrack.alert.service.notification;

import com.mycryptotrack.alert.dto.NotificationDataDto;
import java.util.List;

public interface NotificationService {

    List<NotificationDataDto> getAllNotifications();
    void deleteNotification(Long id);
    void createNotificationForEmail(String email, String message);
    List<NotificationDataDto> getAllNotifications(String email);
}
