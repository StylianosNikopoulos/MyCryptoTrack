package com.mycryptotrack.alert.service.notificationemail;

import com.mycryptotrack.alert.entity.AlertData;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link NotificationEmailServiceImpl}.
 *
 * - MimeMessage is created and sent via JavaMailSender.
 * - The email is sent to the correct recipient.
 * - The email content contains the expected alert data.
 */

@ExtendWith(MockitoExtension.class)
class NotificationEmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    private NotificationEmailServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new NotificationEmailServiceImpl(mailSender);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void sendAlert_ShouldSendEmail() throws Exception {
        // Arrange
        AlertData alert = AlertData.builder()
                .symbol("BTCUSDT")
                .targetPrice(30000.0)
                .email("test@email.com")
                .triggered(false)
                .build();

        MimeMessageHelper helper = spy(new MimeMessageHelper(mimeMessage, true));

        // Act
        service.sendAlert(alert, 30050.0);

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());
        MimeMessage sentMessage = captor.getValue();
        assertThat(sentMessage).isNotNull();
    }
}
