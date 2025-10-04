package com.mycryptotrack.market.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycryptotrack.common.dto.MarketDataDto;
import com.mycryptotrack.market.entity.MarketData;
import com.mycryptotrack.market.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Consumer {
    private final ObjectMapper mapper;
    private final MarketDataRepository repository;

    @KafkaListener(topics = "${market.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        try {
            MarketDataDto dto = mapper.readValue(message, MarketDataDto.class);
            repository.save(
                    MarketData.builder()
                            .symbol(dto.getSymbol())
                            .price(dto.getPrice())
                            .fetchedAt(dto.getFetchedAt())
                            .build()
            );
            log.info("Saved market data: {} - {}", dto.getSymbol(), dto.getPrice());
        } catch (Exception e) {
            log.error("Error consuming message: {}", message, e);
        }
    }
}
