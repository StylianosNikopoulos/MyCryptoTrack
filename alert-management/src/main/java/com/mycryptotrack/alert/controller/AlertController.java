package com.mycryptotrack.alert.controller;

import com.mycryptotrack.alert.dto.AlertDataDto;
import com.mycryptotrack.alert.service.alert.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService service;

    @PostMapping
    public AlertDataDto createAlert(@RequestBody AlertDataDto dto) {
        return service.createAlert(dto.getSymbol(),dto.getTargetPrice(), dto.getEmail());
    }

    @GetMapping
    public List<AlertDataDto> getAllAlerts() {
        return service.getAllAlerts();
    }

    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable Long id) {
        service.deleteAlert(id);
    }
}
