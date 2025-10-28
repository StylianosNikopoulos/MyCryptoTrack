package com.mycryptotrack.alert.service.notificationemail;

import com.mycryptotrack.alert.entity.AlertData;

public interface NotificationEmailService {
    void sendAlert(AlertData alert, double currentPrice);
}
