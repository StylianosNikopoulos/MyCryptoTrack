package com.mycryptotrack.market.service.market;

import com.mycryptotrack.common.dto.MarketDataDto;

import java.util.List;

public interface MarketIngestService {
    List<MarketDataDto> fetchOnceAndReturn();
    List<MarketDataDto> getLatestData();
}
