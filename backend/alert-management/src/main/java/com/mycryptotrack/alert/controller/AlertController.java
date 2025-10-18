package com.mycryptotrack.alert.controller;

import com.mycryptotrack.alert.dto.AlertDataDto;
import com.mycryptotrack.alert.service.alert.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alert")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService service;

    @PostMapping("/create")
    public AlertDataDto createAlert(@RequestBody AlertDataDto dto) {
        return service.createAlert(dto.getSymbol(), dto.getTargetPrice(), dto.getEmail(), dto.getType());
    }

    @GetMapping("allAlerts")
    public List<AlertDataDto> getAllAlerts() {
        return service.getAllAlerts();
    }

    @PutMapping("/update/{id}")
    public AlertDataDto updateAlert(@PathVariable Long id, @RequestBody AlertDataDto dto){
        return service.updateAlert(id,dto.getSymbol(), dto.getTargetPrice(), dto.getEmail(), dto.getType());
    }

    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable Long id) {
        service.deleteAlert(id);
    }
}
