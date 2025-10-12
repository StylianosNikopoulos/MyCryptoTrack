package com.mycryptotrack.market.service.marketstream;

import com.mycryptotrack.common.dto.MarketDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class MarketStreamServiceImpl implements MarketStreamService {

    private final Sinks.Many<MarketDataDto> sink = Sinks.many().multicast().directBestEffort();

    @Override
    public void publish(MarketDataDto data) {
        log.info("Publishing to stream: {} - {}", data.getSymbol(), data.getPrice());
        sink.tryEmitNext(data);
    }

    @Override
    public Flux<MarketDataDto> streamData() {
        log.info("Client connected to /stream endpoint");
        return sink.asFlux();
    }
}
