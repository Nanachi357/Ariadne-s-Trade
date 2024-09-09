package com.freelance.nanachi357.ariadnestrade.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mapping fields from the DTO to the entity
    private String baseCurrency;
    private Double blockTradeCommission;
    private Double blockTradeMinTradeAmount;
    private Double blockTradeTickSize;
    private Integer contractSize;
    private String counterCurrency;

    private LocalDateTime creationTimestamp; // Converted to LocalDateTime
    private LocalDateTime expirationTimestamp; // Converted to LocalDateTime

    private String instrumentName;
    private String instrumentType;
    private Boolean isActive;
    private String kind;
    private Double makerCommission;
    private Integer maxLeverage;
    private Double maxLiquidationCommission;
    private Double minTradeAmount;
    private String optionType;
    private String priceIndex;
    private String quoteCurrency;
    private Boolean rfq;
    private String settlementCurrency;
    private String settlementPeriod;
    private Double strike;
    private Double takerCommission;
    private Double tickSize;

    // Ignoring the deprecated field 'future_type' as it’s not needed in the entity

    // Optionally: Add mapping for TickSizeStep array if necessary for your database design
}
