package com.mycryptotrack.alert.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @KafkaListener(topics = "${market.kafka.topic}", groupId = "alert-group")
    public void listen(String message) {
        try {
            MarketDataDto marketData = mapper.readValue(message, MarketDataDto.class);

            // Check for alerts matching chosen symbol
            repository.findBySymbolAndTriggeredFalse(marketData.getSymbol())
                    .forEach(alert -> {
                        if (marketData.getPrice() >= alert.getTargetPrice()) {
                            alert.setTriggered(true);
                            repository.save(alert);

                            // Trigger notification
                            notificationService.sendAlert(alert, marketData.getPrice());
                            log.info("✅ Alert triggered for {} at {}", alert.getSymbol(), marketData.getPrice());
                        }
                    });

        } catch (Exception e) {
            log.error("❌ Error processing market data: {}", message, e);
        }
    }
}
