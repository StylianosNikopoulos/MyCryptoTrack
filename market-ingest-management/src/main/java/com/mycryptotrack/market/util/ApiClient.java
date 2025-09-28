package com.mycryptotrack.market.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Map;

@Component
public class ApiClient {

    private final WebClient webClient;

    public ApiClient(@Value("${market.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // Fetch all symbols
    public Flux<Map<String, Object>> getAllPrices() {
        return webClient.get()
                .uri("/api/v3/ticker/price")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                .timeout(Duration.ofSeconds(10));
    }
}
