package com.mycryptotrack.market.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycryptotrack.common.dto.MarketDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper mapper;

    private Producer producer;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        producer = new Producer(kafkaTemplate, mapper);

        Field topicField = Producer.class.getDeclaredField("topic");
        topicField.setAccessible(true);
        topicField.set(producer, "test-topic");
    }


    @Test
    void send_ShouldSerializeAndSend() throws Exception {
        MarketDataDto dto = new MarketDataDto("BTCUSDT", 30000.0, null);
        String json = "{\"symbol\":\"BTCUSDT\",\"price\":30000.0}";

        when(mapper.writeValueAsString(dto)).thenReturn(json);

        producer.send(dto);

        verify(kafkaTemplate, times(1)).send("test-topic", "BTCUSDT", json);
    }

    @Test
    void send_ShouldThrowRuntimeException_WhenSerializationFails() throws Exception {
        MarketDataDto dto = new MarketDataDto("BTCUSDT", 30000.0, null);

        when(mapper.writeValueAsString(dto)).thenThrow(new JsonProcessingException("fail") {});

        RuntimeException exception = assertThrows(RuntimeException.class, () -> producer.send(dto));

        assertEquals("Failed to serialize MarketDataDto", exception.getMessage());
    }
}
