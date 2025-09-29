package com.mycryptotrack.alert.service.alert;

import com.mycryptotrack.alert.dto.AlertDataDto;
import java.util.List;

public interface AlertService {
    AlertDataDto createAlert(String symbol, double targetPrice, String email);
    List<AlertDataDto> getAllAlerts();
    void deleteAlert(Long id);
}
