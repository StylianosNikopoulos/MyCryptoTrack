package com.mycryptotrack.alert.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycryptotrack.alert.repository.AlertRepository;
import com.mycryptotrack.alert.service.notificationemail.NotificationEmailService;
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
    private final NotificationEmailService notificationService;
    private final NotificationService notificationDbService;

    @KafkaListener(topics = "${market.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        try {
            MarketDataDto marketData = mapper.readValue(message, MarketDataDto.class);

            repository.findBySymbolAndTriggeredFalse(marketData.getSymbol())
                    .forEach(alert -> {
                        boolean shouldTrigger = switch (alert.getType()) {
                            case SELL -> marketData.getPrice() >= alert.getTargetPrice();
                            case BUY -> marketData.getPrice() <= alert.getTargetPrice();
                        };

                        if (!alert.isTriggered() && shouldTrigger) {
                            alert.setTriggered(true);
                            repository.save(alert);

                            // Send email
                            notificationService.sendAlert(alert, marketData.getPrice());

                            // Create in-app notification
                            String messageText = String.format(
                                    "%s reached your target price of %.2f! Current price: %.2f",
                                    alert.getSymbol(),
                                    alert.getTargetPrice(),
                                    marketData.getPrice()
                            );
                            notificationDbService.createNotification(alert.getEmail(), messageText);

                            log.info("In-app notification created for {}", alert.getEmail());
                        }
                    });

        } catch (Exception e) {
            log.error("Error processing market data: {}", message, e);
        }
    }
}
