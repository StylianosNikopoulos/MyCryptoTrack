package com.mycryptotrack.market.controller;

import com.mycryptotrack.common.dto.MarketDataDto;
import com.mycryptotrack.market.service.market.MarketIngestImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketIngestImpl service;

    @GetMapping("/latest")
    public ResponseEntity<List<MarketDataDto>> latest() {
        return ResponseEntity.ok(service.getLatestData());
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<MarketDataDto>> fetchNow() {
        List<MarketDataDto> data = service.fetchOnceAndReturn();
        return ResponseEntity.ok(data);
    }
}

