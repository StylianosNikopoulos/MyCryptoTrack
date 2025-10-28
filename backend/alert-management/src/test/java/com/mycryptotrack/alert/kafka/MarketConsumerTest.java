package com.mycryptotrack.alert.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycryptotrack.alert.enums.AlertType;
import com.mycryptotrack.alert.entity.AlertData;
import com.mycryptotrack.alert.repository.AlertRepository;
import com.mycryptotrack.alert.service.notification.NotificationService;
import com.mycryptotrack.alert.service.notificationemail.NotificationEmailService;
import com.mycryptotrack.common.dto.MarketDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MarketConsumerTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private AlertRepository repository;

    @Mock
    private NotificationEmailService notificationService;

    private MarketConsumer consumer;

    @Mock
    private NotificationService notificationDbService;

    @BeforeEach
    void setUp() {
        consumer = new MarketConsumer(mapper, repository, notificationService,notificationDbService);
    }

    @Test
    void listen_ShouldTriggerBuyAlert_WhenPriceBelowTarget() throws Exception {
        String message = "{ \"symbol\": \"BTCUSDT\", \"price\": 29000 }";
        MarketDataDto marketData = new MarketDataDto("BTCUSDT", 29000.0, null);
        when(mapper.readValue(message, MarketDataDto.class)).thenReturn(marketData);

        AlertData alert = new AlertData();
        alert.setSymbol("BTCUSDT");
        alert.setTargetPrice(30000.0);
        alert.setTriggered(false);
        alert.setType(AlertType.BUY);
        alert.setEmail("test@email.com");

        when(repository.findBySymbolAndTriggeredFalse("BTCUSDT")).thenReturn(List.of(alert));

        consumer.listen(message);

        assertThat(alert.isTriggered()).isTrue();
        verify(repository).save(alert);
        verify(notificationService).sendAlert(alert, 29000.0);
    }

    @Test
    void listen_ShouldTriggerSellAlert_WhenPriceAboveTarget() throws Exception {
        String message = "{ \"symbol\": \"BTCUSDT\", \"price\": 35000 }";
        MarketDataDto marketData = new MarketDataDto("BTCUSDT", 35000.0, null);
        when(mapper.readValue(message, MarketDataDto.class)).thenReturn(marketData);

        AlertData alert = new AlertData();
        alert.setSymbol("BTCUSDT");
        alert.setTargetPrice(34000.0);
        alert.setTriggered(false);
        alert.setType(AlertType.SELL);
        alert.setEmail("test@email.com");

        when(repository.findBySymbolAndTriggeredFalse("BTCUSDT")).thenReturn(List.of(alert));

        consumer.listen(message);

        assertThat(alert.isTriggered()).isTrue();
        verify(repository).save(alert);
        verify(notificationService).sendAlert(alert, 35000.0);
    }

    @Test
    void listen_ShouldNotTriggerAlert_WhenConditionNotMet() throws Exception {
        String message = "{ \"symbol\": \"BTCUSDT\", \"price\": 31000 }";
        MarketDataDto marketData = new MarketDataDto("BTCUSDT", 31000.0, null);
        when(mapper.readValue(message, MarketDataDto.class)).thenReturn(marketData);

        // BUY alert above current price
        AlertData buyAlert = new AlertData();
        buyAlert.setSymbol("BTCUSDT");
        buyAlert.setTargetPrice(30000.0);
        buyAlert.setTriggered(false);
        buyAlert.setType(AlertType.BUY);

        // SELL alert below current price
        AlertData sellAlert = new AlertData();
        sellAlert.setSymbol("BTCUSDT");
        sellAlert.setTargetPrice(32000.0);
        sellAlert.setTriggered(false);
        sellAlert.setType(AlertType.SELL);

        when(repository.findBySymbolAndTriggeredFalse("BTCUSDT")).thenReturn(List.of(buyAlert, sellAlert));

        consumer.listen(message);

        assertThat(buyAlert.isTriggered()).isFalse();
        assertThat(sellAlert.isTriggered()).isFalse();
        verify(repository, never()).save(any());
        verify(notificationService, never()).sendAlert(any(), anyDouble());
    }

    @Test
    void listen_ShouldCatchException_WhenMapperFails() throws Exception {
        String message = "invalid json";
        when(mapper.readValue(message, MarketDataDto.class)).thenThrow(new RuntimeException("Failed"));

        consumer.listen(message);

        verifyNoInteractions(repository);
        verifyNoInteractions(notificationService);
    }
}
