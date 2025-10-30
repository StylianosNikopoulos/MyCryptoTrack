package com.mycryptotrack.alert.service.notification;

import com.mycryptotrack.alert.dto.NotificationDataDto;
import com.mycryptotrack.alert.entity.NotificationData;
import com.mycryptotrack.alert.repository.NotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationServiceImplTest}.
 *
 * 1. Creating a notification for a given email and saving it correctly.
 * 2. Fetching all notifications for the current authenticated user.
 * 3. Deleting notifications, including authorization checks.
 *
 */

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Jwt jwt;

    @Mock
    private Authentication authentication;

    private NotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new NotificationServiceImpl(repository);

        SecurityContextHolder.setContext(securityContext);

        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(jwt);
        lenient().when(jwt.getClaim("email")).thenReturn("test@email.com");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createNotificationForEmail_ShouldSaveNotification() {
        String email = "test@email.com";
        String message = "Test notification";

        NotificationData savedNotif = NotificationData.builder()
                .id(1L)
                .email(email)
                .message(message)
                .createdAt(Instant.now())
                .build();

        when(repository.saveAndFlush(any(NotificationData.class))).thenReturn(savedNotif);

        service.createNotificationForEmail(email, message);

        ArgumentCaptor<NotificationData> captor = ArgumentCaptor.forClass(NotificationData.class);
        verify(repository, times(1)).saveAndFlush(captor.capture());

        NotificationData captured = captor.getValue();
        assertThat(captured.getEmail()).isEqualTo(email);
        assertThat(captured.getMessage()).isEqualTo(message);
        assertThat(captured.getCreatedAt()).isNotNull();
    }

    @Test
    void getAllNotifications_ShouldReturnDtosForCurrentUser() {
        NotificationData notif = NotificationData.builder()
                .id(1L)
                .email("test@email.com")
                .message("Test")
                .createdAt(Instant.now())
                .build();

        when(repository.findByEmailOrderByCreatedAtDesc("test@email.com"))
                .thenReturn(List.of(notif));

        List<NotificationDataDto> dtos = service.getAllNotifications();

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getMessage()).isEqualTo("Test");
    }

    @Test
    void deleteNotification_ShouldThrow_WhenNotAuthorized() {
        NotificationData notif = NotificationData.builder()
                .id(1L)
                .email("other@email.com")
                .message("Test")
                .createdAt(Instant.now())
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(notif));

        when(jwt.getClaim("email")).thenReturn("test@email.com");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteNotification(1L));
        assertThat(ex.getMessage()).isEqualTo("Unauthorized");
    }

    @Test
    void deleteNotification_ShouldDelete_WhenAuthorized() {
        NotificationData notif = NotificationData.builder()
                .id(1L)
                .email("test@email.com")
                .message("Test")
                .createdAt(Instant.now())
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(notif));

        service.deleteNotification(1L);

        verify(repository, times(1)).delete(notif);
    }
}
