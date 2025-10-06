package com.mycryptotrack.common.dto;


import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketDataDto {
    private String symbol;
    private double price;
    private Instant fetchedAt;
}
