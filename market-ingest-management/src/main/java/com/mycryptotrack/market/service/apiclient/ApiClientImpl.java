package com.mycryptotrack.market.service.apiclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Map;

@Component
public class ApiClientImpl implements ApiClientService {

    private final WebClient webClient;

    public ApiClientImpl(@Value("${market.api.base-url}") String baseUrl) {
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
