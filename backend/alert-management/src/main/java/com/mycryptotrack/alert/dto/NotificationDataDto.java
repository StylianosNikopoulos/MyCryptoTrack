package com.mycryptotrack.alert.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class NotificationDataDto {
    private Long id;
    private String message;
    private Instant createdAt;
}
