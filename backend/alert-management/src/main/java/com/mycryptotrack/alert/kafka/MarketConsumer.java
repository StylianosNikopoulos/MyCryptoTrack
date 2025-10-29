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

            repository.findBySymbol(marketData.getSymbol())
                    .forEach(alert -> {
                        boolean shouldTrigger = switch (alert.getType()) {
                            case SELL -> marketData.getPrice() >= alert.getTargetPrice();
                            case BUY -> marketData.getPrice() <= alert.getTargetPrice();
                        };

                        if (shouldTrigger) {
                            if (!alert.isTriggered()) {
                                alert.setTriggered(true);
                                repository.save(alert);
                                notificationService.sendAlert(alert, marketData.getPrice());
                            }

                            // Create notification only if not exists
                            String messageText = String.format(
                                    "%s reached your target price of %.2f! Current price: %.2f",
                                    alert.getSymbol(),
                                    alert.getTargetPrice(),
                                    marketData.getPrice()
                            );

                            boolean exists = notificationDbService.getAllNotifications(alert.getEmail())
                                    .stream()
                                    .anyMatch(n -> n.getMessage().equals(messageText));

                            if (!exists) {
                                notificationDbService.createNotificationForEmail(alert.getEmail(), messageText);
                            }
                        }
                    });
        } catch (Exception e) {
            log.error("Error processing market data: {}", message, e);
        }
    }
}
