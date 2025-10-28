package com.mycryptotrack.alert.service.notification;

import com.mycryptotrack.alert.dto.NotificationDataDto;
import com.mycryptotrack.alert.entity.NotificationData;
import com.mycryptotrack.alert.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository repository;

    @Override
    public List<NotificationDataDto> getAllNotifications() {
        String email = getUserEmail();

        return repository.findByEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDataDto createNotification(String email, String message) {
        NotificationData n = NotificationData.builder()
                .email(email)
                .message(message)
                .read(false)
                .createdAt(Instant.now())
                .build();

        return toDto(repository.save(n));
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

    @Override
    public NotificationDataDto markAsRead(Long id) {
        String email = getUserEmail();
        NotificationData notif = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if(!notif.getEmail().equalsIgnoreCase(email))
            throw new RuntimeException("Unauthorized");
        notif.setRead(true);

        return toDto(repository.save(notif));
    }

    private NotificationDataDto toDto(NotificationData n){
        return NotificationDataDto.builder()
                .id(n.getId())
                .message(n.getMessage())
                .read(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }

    private String getUserEmail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();

        return jwt.getClaim("email") != null
                ? jwt.getClaim("email").toString()
                : jwt.getSubject();
    }
}
