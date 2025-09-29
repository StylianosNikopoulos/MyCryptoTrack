package com.mycryptotrack.alert.service.notification;

import com.mycryptotrack.alert.model.AlertData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendAlert(AlertData alert, double currentPrice) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(alert.getEmail());
            helper.setSubject("Crypto Alert - " + alert.getSymbol());
            helper.setText(
                    String.format("🚨 %s has reached your target price of %.2f! Current price: %.2f",
                            alert.getSymbol(), alert.getTargetPrice(), currentPrice),
                    false
            );

            mailSender.send(message);
            log.info("📧 Email sent for alert {}", alert.getSymbol());

        } catch (MessagingException e) {
            log.error("Failed to send email alert for {}", alert.getSymbol(), e);
        }
    }
}
