package com.mycryptotrack.alert.repository;

import com.mycryptotrack.alert.model.AlertData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<AlertData, Long> {
    List<AlertData> findBySymbolAndTriggeredFalse(String symbol);
}
