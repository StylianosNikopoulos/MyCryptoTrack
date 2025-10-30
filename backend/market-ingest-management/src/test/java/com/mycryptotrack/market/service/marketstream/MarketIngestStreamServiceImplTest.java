package com.mycryptotrack.market.service.marketstream;

import com.mycryptotrack.common.dto.MarketDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Unit tests for {@link MarketStreamServiceImpl}.
 *
 * 1. Ensures published market data is emitted to subscribers.
 * 2. Verifies multiple data points are emitted in order.
 * 3. Confirms that the stream does not emit data before publishing.
 */

class MarketIngestStreamServiceImplTest {

    private MarketStreamServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MarketStreamServiceImpl();
    }

    @Test
    void publish_ShouldEmitDataToSubscribers() {
        MarketDataDto data = new MarketDataDto("BTCUSDT", 30000.0, null);

        Flux<MarketDataDto> flux = service.streamData();

        StepVerifier.create(flux)
                .then(() -> service.publish(data))
                .expectNextMatches(marketData ->
                        marketData.getSymbol().equals("BTCUSDT") &&
                                marketData.getPrice() == 30000.0
                )
                .thenCancel()
                .verify();
    }

    @Test
    void multiplePublishes_ShouldEmitAllData() {
        MarketDataDto data1 = new MarketDataDto("BTCUSDT", 30000.0, null);
        MarketDataDto data2 = new MarketDataDto("ETHUSDT", 2000.0, null);

        Flux<MarketDataDto> flux = service.streamData();

        StepVerifier.create(flux)
                .then(() -> {
                    service.publish(data1);
                    service.publish(data2);
                })
                .expectNextMatches(d -> d.getSymbol().equals("BTCUSDT") && d.getPrice() == 30000.0)
                .expectNextMatches(d -> d.getSymbol().equals("ETHUSDT") && d.getPrice() == 2000.0)
                .thenCancel()
                .verify();
    }

    @Test
    void streamData_ShouldNotEmitBeforePublish() {
        Flux<MarketDataDto> flux = service.streamData();

        StepVerifier.create(flux)
                .expectSubscription()
                .thenCancel()
                .verify();
    }
}
