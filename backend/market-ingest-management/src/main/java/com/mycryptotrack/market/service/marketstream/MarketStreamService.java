package com.mycryptotrack.market.service.marketstream;

import com.mycryptotrack.common.dto.MarketDataDto;
import reactor.core.publisher.Flux;

public interface MarketStreamService {
    void publish(MarketDataDto data);
    Flux<MarketDataDto> streamData();
}
