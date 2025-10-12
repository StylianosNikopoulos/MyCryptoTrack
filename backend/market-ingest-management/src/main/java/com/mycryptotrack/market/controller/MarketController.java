package com.mycryptotrack.market.controller;

import com.mycryptotrack.common.dto.MarketDataDto;
import com.mycryptotrack.market.service.market.MarketIngestServiceImpl;
import com.mycryptotrack.market.service.marketstream.MarketStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketIngestServiceImpl service;
    private final MarketStreamService marketStreamService;

    @GetMapping("/latest")
    public ResponseEntity<List<MarketDataDto>> latest() {
        return ResponseEntity.ok(service.getLatestDataFromDB());
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<MarketDataDto>> fetchNow() {
        List<MarketDataDto> data = service.fetchOnceAndReturn();
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MarketDataDto> streamMarketData() {
        return marketStreamService.streamData();
    }

}

