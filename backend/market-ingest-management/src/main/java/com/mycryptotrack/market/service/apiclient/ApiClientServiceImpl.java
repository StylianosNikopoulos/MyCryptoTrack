package com.mycryptotrack.market.service.apiclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Map;

@Service
@Slf4j
public class ApiClientServiceImpl implements ApiClientService {

    private final WebClient webClient;

    public ApiClientServiceImpl(@Value("${market.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // Fetch all symbols
    @Override
    public Flux<Map<String, Object>> getAllPrices() {
        return webClient.get()
                .uri("/api/v3/ticker/price")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                .timeout(Duration.ofSeconds(10));
    }

    @Override
    public Flux<Object[]> getKlines(String symbol, String interval, int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v3/klines")
                        .queryParam("symbol", symbol)
                        .queryParam("interval", interval)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToFlux(Object[].class)
                .timeout(Duration.ofSeconds(10));
    }
}
