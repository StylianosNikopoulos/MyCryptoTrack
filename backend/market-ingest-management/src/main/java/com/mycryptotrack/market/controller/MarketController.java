package com.mycryptotrack.market.controller;

import com.mycryptotrack.common.dto.MarketDataDto;
import com.mycryptotrack.market.service.market.MarketIngestServiceImpl;
import com.mycryptotrack.market.service.marketstream.MarketStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    public List<MarketDataDto> latest() {
        return service.getLatestDataFromDB();
    }

    @GetMapping("/fetch")
    public List<MarketDataDto> fetchNow() {
        return service.fetchOnceAndReturn();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MarketDataDto> streamMarketData() {
        return marketStreamService.streamData();
    }
}
