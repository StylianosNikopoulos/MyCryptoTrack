package com.mycryptotrack.alert.service.notification;

import com.mycryptotrack.alert.model.AlertData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationImpl implements NotificationService {

    @Override
    public void sendAlert(AlertData alert, double currentPrice) {
        log.info("ALERT! {} reached target {}. Current price: {}",
                alert.getSymbol(), alert.getTargetPrice(), currentPrice);
    }
}
