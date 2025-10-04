package com.mycryptotrack.alert.service.notification;

import com.mycryptotrack.alert.entity.AlertData;

public interface NotificationService {
    void sendAlert(AlertData alert, double currentPrice);
}
