package com.mycryptotrack.market.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "market_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketData {

    @Id
    @Column(nullable = false, unique = true)
    private String symbol;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private Instant fetchedAt;
}
