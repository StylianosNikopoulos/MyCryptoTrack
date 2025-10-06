package com.mycryptotrack.market.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycryptotrack.common.dto.MarketDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Value("${market.kafka.topic}")
    private String topic;

    public void send(MarketDataDto dto) {
        try {
            String payload = mapper.writeValueAsString(dto);
            kafkaTemplate.send(topic, dto.getSymbol(), payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize MarketDataDto", e);
        }
    }
}
