package com.mycryptotrack.market.repository;

import com.mycryptotrack.market.model.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
    Optional<MarketData> findBySymbol(String symbol);
}
