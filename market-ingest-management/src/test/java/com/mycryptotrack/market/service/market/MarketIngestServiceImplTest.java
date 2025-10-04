package com.mycryptotrack.market.service.market;

import com.mycryptotrack.common.dto.MarketDataDto;
import com.mycryptotrack.market.kafka.Producer;
import com.mycryptotrack.market.model.MarketData;
import com.mycryptotrack.market.repository.MarketDataRepository;
import com.mycryptotrack.market.service.apiclient.ApiClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MarketIngestServiceImplTest {

    @Mock
    private ApiClientServiceImpl apiClient;

    @Mock
    private Producer producer;

    @Mock
    private MarketDataRepository repository;

    private MarketIngestServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new MarketIngestServiceImpl(apiClient, producer, repository);
    }

    @Test
    void fetchOnceAndReturn_ShouldSaveAndSendData() throws InterruptedException {
        Map<String, Object> data = Map.of("symbol", "BTCUSDT", "price", 30000.0);
        when(apiClient.getAllPrices()).thenReturn(Flux.just(data));
        when(repository.findBySymbol("BTCUSDT")).thenReturn(Optional.empty());

        service.fetchOnceAndReturn();

        Thread.sleep(100);

        verify(producer, times(1)).send(any(MarketDataDto.class));
        verify(repository, times(1)).save(any(MarketData.class));

        assertTrue(service.fetchOnceAndReturn()
                .stream()
                .anyMatch(dto -> "BTCUSDT".equals(dto.getSymbol())));
    }

    @Test
    void getLatestDataFromDB_ShouldReturnPersistedData() {
        MarketData md = MarketData.builder()
                .symbol("BTCUSDT")
                .price(30000.0)
                .fetchedAt(Instant.now())
                .build();

        when(repository.findAll()).thenReturn(List.of(md));

        List<MarketDataDto> latest = service.getLatestDataFromDB();

        assertTrue(latest.stream().anyMatch(d -> "BTCUSDT".equals(d.getSymbol())));
    }
}
