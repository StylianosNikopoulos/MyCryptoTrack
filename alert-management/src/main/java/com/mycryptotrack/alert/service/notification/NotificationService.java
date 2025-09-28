package com.mycryptotrack.alert.service.notification;

import com.mycryptotrack.alert.model.AlertData;

public interface NotificationService {
    void sendAlert(AlertData alert, double currentPrice);
}
