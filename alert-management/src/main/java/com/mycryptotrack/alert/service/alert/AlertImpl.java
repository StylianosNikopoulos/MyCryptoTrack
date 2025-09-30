package com.mycryptotrack.alert.service.alert;

import com.mycryptotrack.alert.dto.AlertDataDto;
import com.mycryptotrack.alert.model.AlertData;
import com.mycryptotrack.alert.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertImpl implements AlertService {
    private final AlertRepository repository;

    @Override
    public AlertDataDto createAlert(String symbol, double targetPrice, String ignoredEmail) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();

        String email = jwt.getSubject();

        AlertData alert = AlertData.builder()
                .symbol(symbol)
                .targetPrice(targetPrice)
                .triggered(false)
                .email(email)
                .fetchedAt(Instant.now())
                .build();

        AlertData saved = repository.save(alert);
        return mapToDto(saved);
    }

    @Override
    public List<AlertDataDto> getAllAlerts() {
        return repository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAlert(Long id) {
        repository.deleteById(id);
    }

    private AlertDataDto mapToDto(AlertData alert) {
        return AlertDataDto.builder()
                .symbol(alert.getSymbol())
                .targetPrice(alert.getTargetPrice())
                .triggered(alert.isTriggered())
                .email(alert.getEmail())
                .build();
    }
}
