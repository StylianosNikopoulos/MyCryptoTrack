package com.mycryptotrack.alert.service.alert;

import com.mycryptotrack.alert.dto.AlertDataDto;
import com.mycryptotrack.alert.enums.AlertType;

import javax.accessibility.AccessibleKeyBinding;
import java.util.List;

public interface AlertService {
    AlertDataDto createAlert(String symbol, double targetPrice, String email, AlertType type);
    AlertDataDto updateAlert(Long id, String symbol, double targetPrice, String email, AlertType type);
    List<AlertDataDto> getAllAlerts();
    void deleteAlert(Long id);
}
