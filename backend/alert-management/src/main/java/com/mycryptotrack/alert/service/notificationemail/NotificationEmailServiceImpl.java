package com.mycryptotrack.alert.service.notificationemail;

import com.mycryptotrack.alert.entity.AlertData;
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
public class NotificationEmailServiceImpl implements NotificationEmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendAlert(AlertData alert, double currentPrice) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(alert.getEmail());
            helper.setSubject("Crypto Alert - " + alert.getSymbol());
            helper.setText(
                    "<h2>🚨 Crypto Alert</h2>" +
                            "<p><b>" + alert.getSymbol() + "</b> has reached your target price of <b>" + alert.getTargetPrice() +
                            "</b>! Current price: <b>" + currentPrice + "</b></p>",
                    true
            );

            mailSender.send(message);
            log.info("Email sent for alert {}", alert.getSymbol());

        } catch (MessagingException e) {
            log.error("Failed to send email alert for {}", alert.getSymbol(), e);
        }
    }
}
