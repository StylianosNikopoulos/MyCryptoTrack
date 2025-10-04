package com.mycryptotrack.market.service.market;

import com.mycryptotrack.common.dto.MarketDataDto;
import com.mycryptotrack.market.exception.CustomException;
import com.mycryptotrack.market.kafka.Producer;
import com.mycryptotrack.market.service.apiclient.ApiClientServiceImpl;
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
public class MarketIngestServiceImpl implements MarketIngestService {

    private final ApiClientServiceImpl apiClient;
    private final Producer producer;
    private final MarketDataRepository repository;
    private final List<MarketDataDto> latestData = new CopyOnWriteArrayList<>();

    @Scheduled(fixedRateString = "${market.fetch.interval}")
    public void scheduledFetch() {
        fetchAllSymbolsAsync();
    }

    public List<MarketDataDto> fetchOnceAndReturn() {
        fetchAllSymbolsAsync();
        return List.copyOf(latestData);
    }

    public List<MarketDataDto> getLatestData() {
        return List.copyOf(latestData);
    }

    private void fetchAllSymbolsAsync() {
        Flux<Map<String, Object>> allPrices = apiClient.getAllPrices();

        allPrices
                .map(this::mapToEntityAndDto)
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

    private MarketData mapToEntityAndDto(Map<String, Object> data) {
        try {
            String symbol = data.get("symbol").toString();
            double price = Double.parseDouble(data.get("price").toString());
            Instant fetchedAt = Instant.now();

            // Create DTO
            MarketDataDto dto = MarketDataDto.builder()
                    .symbol(symbol)
                    .price(price)
                    .fetchedAt(fetchedAt)
                    .build();

            // Send to Kafka
            producer.send(dto);

            // Update latestData cache
            latestData.removeIf(d -> d.getSymbol().equals(symbol));
            latestData.add(dto);

            // Return entity
            return MarketData.builder()
                    .symbol(symbol)
                    .price(price)
                    .fetchedAt(fetchedAt)
                    .build();

        } catch (Exception ex) {
            log.error("Error processing symbol: {}", data.get("symbol"), ex);
            throw new CustomException("Failed to process symbol: " + data.get("symbol"));
        }
    }
}

