package com.mycryptotrack.market.service.apiclient;

import reactor.core.publisher.Flux;
import java.util.Map;

public interface ApiClientService {
    Flux<Map<String, Object>> getAllPrices();
}
