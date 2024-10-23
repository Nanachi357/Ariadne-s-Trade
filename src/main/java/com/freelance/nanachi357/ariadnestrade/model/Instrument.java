package com.freelance.nanachi357.ariadnestrade.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@Table("instruments") // R2DBC mapping annotation for table name
public class Instrument {

    @Id
    private Long id; // Unique identifier for the instrument

    @NotNull(message = "Base currency cannot be null")
    @Size(min = 3, max = 3, message = "Base currency must be a valid ISO currency code")
    @Column("base_currency")
    private String baseCurrency; // ISO code of the base currency

    @Min(value = 0, message = "Block trade commission must be greater than or equal to 0")
    @Column("block_trade_commission")
    private Double blockTradeCommission; // Commission for block trades

    @Min(value = 0, message = "Block trade min trade amount must be greater than or equal to 0")
    @Column("block_trade_min_trade_amount")
    private Double blockTradeMinTradeAmount; // Minimum trade amount for block trades

    @Min(value = 0, message = "Block trade tick size must be greater than or equal to 0")
    @Column("block_trade_tick_size")
    private Double blockTradeTickSize; // Tick size for block trades

    @NotNull(message = "Contract size cannot be null")
    @Min(value = 1, message = "Contract size must be greater than or equal to 1")
    @Column("contract_size")
    private Integer contractSize; // Size of the contract

    @NotNull(message = "Counter currency cannot be null")
    @Size(min = 3, max = 3, message = "Counter currency must be a valid ISO currency code")
    @Column("counter_currency")
    private String counterCurrency; // ISO code of the counter currency

    @NotNull(message = "Creation timestamp cannot be null")
    @Column("creation_timestamp")
    private Instant creationTimestamp; // Timestamp of instrument creation

    @NotNull(message = "Expiration timestamp cannot be null")
    @Column("expiration_timestamp")
    private Instant expirationTimestamp; // Timestamp of instrument expiration

    @NotNull(message = "Instrument name cannot be null")
    @Size(min = 1, max = 255, message = "Instrument name must be between 1 and 255 characters")
    @Column("instrument_name")
    private String instrumentName; // Name of the instrument

    @NotNull(message = "Instrument type cannot be null")
    @Size(min = 1, max = 100, message = "Instrument type must be between 1 and 100 characters")
    @Column("instrument_type")
    private String instrumentType; // Type of the instrument (e.g., future, option)

    @NotNull(message = "Is active cannot be null")
    @Column("is_active")
    private Boolean isActive; // Flag indicating if the instrument is active

    @Size(max = 50, message = "Kind must be up to 50 characters")
    @Column("kind")
    private String kind; // Kind of the instrument, optional

    @NotNull(message = "Maker commission cannot be null")
    @Min(value = 0, message = "Maker commission must be greater than or equal to 0")
    @Column("maker_commission")
    private Double makerCommission; // Maker commission rate

    @Min(value = 0, message = "Max leverage must be greater than or equal to 0")
    @Column("max_leverage")
    private Integer maxLeverage; // Maximum leverage allowed

    @Min(value = 0, message = "Max liquidation commission must be greater than or equal to 0")
    @Column("max_liquidation_commission")
    private Double maxLiquidationCommission; // Maximum liquidation commission rate

    @Min(value = 0, message = "Min trade amount must be greater than or equal to 0")
    @Column("min_trade_amount")
    private Double minTradeAmount; // Minimum trade amount

    @Size(max = 50, message = "Option type must be up to 50 characters")
    @Column("option_type")
    private String optionType; // Type of the option (e.g., call, put)

    @Size(max = 50, message = "Price index must be up to 50 characters")
    @Column("price_index")
    private String priceIndex; // Price index related to the instrument

    @NotNull(message = "Quote currency cannot be null")
    @Size(min = 3, max = 3, message = "Quote currency must be a valid ISO currency code")
    @Column("quote_currency")
    private String quoteCurrency; // ISO code of the quote currency

    @Column("rfq")
    private Boolean rfq; // Request for quote flag, optional

    @NotNull(message = "Settlement currency cannot be null")
    @Size(min = 3, max = 3, message = "Settlement currency must be a valid ISO currency code")
    @Column("settlement_currency")
    private String settlementCurrency; // ISO code of the settlement currency

    @NotNull(message = "Settlement period cannot be null")
    @Size(min = 1, max = 100, message = "Settlement period must be between 1 and 100 characters")
    @Column("settlement_period")
    private String settlementPeriod; // Settlement period description

    @Min(value = 0, message = "Strike must be greater than or equal to 0")
    @Column("strike")
    private Double strike; // Strike price of the instrument, optional for options

    @NotNull(message = "Taker commission cannot be null")
    @Min(value = 0, message = "Taker commission must be greater than or equal to 0")
    @Column("taker_commission")
    private Double takerCommission; // Taker commission rate

    @Min(value = 0, message = "Tick size must be greater than or equal to 0")
    @Column("tick_size")
    private Double tickSize; // Minimum price increment
}
