package com.mycryptotrack.alert.service.notification;

import com.mycryptotrack.alert.dto.NotificationDataDto;
import com.mycryptotrack.alert.entity.NotificationData;
import com.mycryptotrack.alert.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public List<NotificationDataDto> getAllNotifications() {
        String email = getUserEmail();
        return getAllNotifications(email);
    }

    @Override
    public void deleteNotification(Long id) {
        String email = getUserEmail();
        NotificationData notif = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notif.getEmail().equalsIgnoreCase(email))
            throw new RuntimeException("Unauthorized");

        repository.delete(notif);
    }

    // Kafka/system
    @Override
    @Transactional
    public void createNotificationForEmail(String email, String message) {
        NotificationData n = NotificationData.builder()
                .email(email)
                .message(message)
                .createdAt(Instant.now())
                .build();

        NotificationData saved = repository.saveAndFlush(n);
        toDto(saved);
    }

    @Override
    public List<NotificationDataDto> getAllNotifications(String email) {
        return repository.findByEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private NotificationDataDto toDto(NotificationData n) {
        return NotificationDataDto.builder()
                .id(n.getId())
                .message(n.getMessage())
                .createdAt(n.getCreatedAt())
                .build();
    }

    private String getUserEmail() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt)) {
            throw new RuntimeException("No authenticated user found");
        }
        return jwt.getClaim("email") != null ? jwt.getClaim("email").toString() : jwt.getSubject();
    }
}
