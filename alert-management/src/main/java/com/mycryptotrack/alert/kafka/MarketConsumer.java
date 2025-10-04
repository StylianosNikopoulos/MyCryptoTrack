package com.mycryptotrack.alert.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycryptotrack.alert.enums.AlertType;
import com.mycryptotrack.alert.repository.AlertRepository;
import com.mycryptotrack.alert.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.mycryptotrack.common.dto.MarketDataDto;


@Service
@RequiredArgsConstructor
@Slf4j
public class MarketConsumer {

    private final ObjectMapper mapper;
    private final AlertRepository repository;
    private final NotificationService notificationService;
    private AlertType type;

    @KafkaListener(topics = "${market.kafka.topic}", groupId = "alert-group")
    public void listen(String message) {
        try {
            MarketDataDto marketData = mapper.readValue(message, MarketDataDto.class);

            // Check for alerts matching chosen symbol
            repository.findBySymbolAndTriggeredFalse(marketData.getSymbol())
                    .forEach(alert -> {
                        boolean shouldTrigger = switch (alert.getType()) {
                            case SELL -> marketData.getPrice() >= alert.getTargetPrice();
                            case BUY -> marketData.getPrice() <= alert.getTargetPrice();
                        };

                        if (!alert.isTriggered() && shouldTrigger) {
                            alert.setTriggered(true);
                            repository.save(alert);
                            notificationService.sendAlert(alert, marketData.getPrice());
                        }
                    });

        } catch (Exception e) {
            log.error("❌ Error processing market data: {}", message, e);
        }
    }
}
