package com.mycryptotrack.market.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycryptotrack.common.dto.MarketDataDto;
import com.mycryptotrack.market.entity.MarketData;
import com.mycryptotrack.market.repository.MarketDataRepository;
import com.mycryptotrack.market.service.marketstream.MarketStreamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.Instant;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConsumerTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private MarketDataRepository repository;

    @Mock
    private MarketStreamService marketStreamService;

    private Consumer consumer;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        consumer = new Consumer(mapper, repository, marketStreamService);
    }

    @Test
    void listen_ShouldDeserializeAndSave() throws Exception {
        String message = "{\"symbol\":\"BTCUSDT\",\"price\":30000.0,\"fetchedAt\":\"2025-10-04T00:00:00Z\"}";
        MarketDataDto dto = new MarketDataDto("BTCUSDT", 30000.0, Instant.parse("2025-10-04T00:00:00Z"));

        when(mapper.readValue(message, MarketDataDto.class)).thenReturn(dto);

        consumer.listen(message);

        verify(repository, times(1)).save(any(MarketData.class));

        verify(marketStreamService, times(1)).publish(dto);
    }

    @Test
    void listen_ShouldHandleInvalidJson() throws Exception {
        String invalidMessage = "{ invalid json }";

        when(mapper.readValue(invalidMessage, MarketDataDto.class)).thenThrow(new RuntimeException("Invalid JSON"));

        consumer.listen(invalidMessage);

        verify(repository, never()).save(any());

        verify(marketStreamService, never()).publish(any());
    }
}
