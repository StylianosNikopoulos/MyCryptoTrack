package com.mycryptotrack.alert.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "alert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private double targetPrice;

    @Column(nullable = false)
    private boolean triggered;

    @Column(nullable = false)
    private Instant fetchedAt;
}
