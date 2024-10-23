package com.freelance.nanachi357.ariadnestrade.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@Table("trades") // Using @Table annotation for R2DBC mapping
public class Trade {

    @Id
    private Long id; // Using @Id from Spring Data for R2DBC

    @Column("trade_seq")
    private long tradeSeq;

    @Column("trade_id")
    private String tradeId;

    @NotNull(message = "Timestamp cannot be null")
    @Column("timestamp")
    private Instant timestamp; // Changed to Instant for better R2DBC support

    @Column("tick_direction")
    private int tickDirection;

    @NotNull(message = "Price cannot be null")
    @Column("price")
    private double price;

    @Column("mark_price")
    private double markPrice;

    @Column("iv")
    private double iv; // Implied Volatility

    @NotNull(message = "Instrument name cannot be null")
    @Column("instrument_name")
    private String instrumentName; // Useful for linking to Instrument entity

    @Column("index_price")
    private double indexPrice;

    @Column("direction")
    private String direction;

    @Column("amount")
    private double amount;

    // Instead of JPA relation with Instrument, store the instrument ID
    @NotNull(message = "Instrument ID cannot be null")
    @Column("instrument_id")
    private Long instrumentId;
}
