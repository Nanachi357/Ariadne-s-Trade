package com.freelance.nanachi357.ariadnestrade.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fields from TradeDTO
    private long tradeSeq;
    private String tradeId;

    private LocalDateTime timestamp; // Converted to LocalDateTime for better handling

    private int tickDirection;
    private double price;
    private double markPrice;
    private double iv; // Implied Volatility
    private String instrumentName; // Could link this to Instrument entity if needed
    private double indexPrice;
    private String direction;
    private double amount;

    // Relationship with Instrument entity
    @ManyToOne
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;}
