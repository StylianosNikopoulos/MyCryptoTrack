package com.mycryptotrack.alert.dto;

import com.mycryptotrack.alert.enums.AlertType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDataDto {
    private Long id;
    private String symbol;
    private double targetPrice;
    private boolean triggered;
    private String email;
    private AlertType type;
}
