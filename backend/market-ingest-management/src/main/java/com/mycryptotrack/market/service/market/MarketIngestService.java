package com.mycryptotrack.market.service.market;

import com.mycryptotrack.common.dto.MarketDataDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MarketIngestService {
    List<MarketDataDto> fetchOnceAndReturn();
    List<MarketDataDto> getLatestDataFromDB();
    Flux<MarketDataDto> getHistoryForSymbol(String symbol, int limit);
}
