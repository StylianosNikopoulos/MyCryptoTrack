package com.mycryptotrack.alert.repository;

import com.mycryptotrack.alert.entity.AlertData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<AlertData, Long> {
    List<AlertData> findByEmail(String email);
    List<AlertData> findBySymbol(String symbol);
}
