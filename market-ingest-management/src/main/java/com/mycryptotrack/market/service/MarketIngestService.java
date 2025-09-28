package com.mycryptotrack.market.service;

import com.mycryptotrack.market.dto.MarketDataDto;
import com.mycryptotrack.market.exception.CustomException;
import com.mycryptotrack.market.kafka.Producer;
import com.mycryptotrack.market.util.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import com.mycryptotrack.market.model.MarketData ;
import com.mycryptotrack.market.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketIngestService {

    private final ApiClient apiClient;
    private final Producer producer;
    private final MarketDataRepository repository;
    private final List<MarketDataDto> latestData = new CopyOnWriteArrayList<>();

    @Scheduled(fixedRateString = "${market.fetch.interval}")
    public void scheduledFetch() {
        fetchAllSymbolsAsync();
    }

    private void fetchAllSymbolsAsync() {
        Flux<Map<String, Object>> allPrices = apiClient.getAllPrices();

        allPrices
                .map(data -> {
                    try {
                        String symbol = data.get("symbol").toString();
                        double price = Double.parseDouble(data.get("price").toString());

                        MarketDataDto dto = MarketDataDto.builder()
                                .symbol(symbol)
                                .price(price)
                                .fetchedAt(Instant.now())
                                .build();

                        // Send to Kafka
                        producer.send(dto);

                        // Update latestData
                        latestData.removeIf(d -> d.getSymbol().equals(symbol));
                        latestData.add(dto);

                        MarketData entity = MarketData.builder()
                                .symbol(symbol)
                                .price(price)
                                .fetchedAt(dto.getFetchedAt())
                                .build();

                        return entity;
                    } catch (Exception ex) {
                        log.error("Error processing symbol: {}", data.get("symbol"), ex);
                        throw new CustomException("Failed to process symbol: " + data.get("symbol"));
                    }
                })
                .collectList()
                .doOnNext(entities -> {
                    entities.forEach(entity -> {
                        repository.findBySymbol(entity.getSymbol())
                                .ifPresentOrElse(existing -> {
                                    existing.setPrice(entity.getPrice());
                                    existing.setFetchedAt(entity.getFetchedAt());
                                    repository.save(existing);
                                }, () -> repository.save(entity));
                    });
                    log.info("Saved/updated {} market entries to DB", entities.size());
                })

                .doOnError(ex -> log.error("Error fetching Binance data", ex))
                .subscribe();
    }

    public List<MarketDataDto> fetchOnceAndReturn() {
        fetchAllSymbolsAsync();
        return List.copyOf(latestData);
    }

    public List<MarketDataDto> getLatestData() {
        return List.copyOf(latestData);
    }
}


