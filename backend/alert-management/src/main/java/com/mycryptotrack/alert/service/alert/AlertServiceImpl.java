package com.mycryptotrack.alert.service.alert;

import com.mycryptotrack.alert.dto.AlertDataDto;
import com.mycryptotrack.alert.enums.AlertType;
import com.mycryptotrack.alert.entity.AlertData;
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
public class AlertServiceImpl implements AlertService {
    private final AlertRepository repository;

    @Override
    public AlertDataDto createAlert(String symbol, double targetPrice, String ignoredEmail, AlertType type) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();

        String email = jwt.getClaim("email") != null
                ? jwt.getClaim("email").toString()
                : jwt.getSubject();

        AlertData alert = AlertData.builder()
                .symbol(symbol)
                .targetPrice(targetPrice)
                .triggered(false)
                .email(email)
                .type(type)
                .fetchedAt(Instant.now())
                .build();

        AlertData saved = repository.save(alert);
        return mapToDto(saved);
    }


    @Override
    public List<AlertDataDto> getAllAlerts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();

        String email = jwt.getClaim("email") != null
                ? jwt.getClaim("email").toString()
                : jwt.getSubject();

        return repository.findByEmail(email)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteAlert(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();

        String email = jwt.getClaim("email") != null
                ? jwt.getClaim("email").toString()
                : jwt.getSubject();

        AlertData alert = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        repository.delete(alert);
    }


    private AlertDataDto mapToDto(AlertData alert) {
        return AlertDataDto.builder()
                .symbol(alert.getSymbol())
                .targetPrice(alert.getTargetPrice())
                .triggered(alert.isTriggered())
                .email(alert.getEmail())
                .type(alert.getType())
                .build();
    }
}
