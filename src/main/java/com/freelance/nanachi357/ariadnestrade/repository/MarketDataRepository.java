package com.freelance.nanachi357.ariadnestrade.repository;

import com.freelance.nanachi357.ariadnestrade.model.Trade;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Repository
@Validated
public interface MarketDataRepository extends R2dbcRepository<Trade, Long> {

    // Fetch trades by instrument ID
    Flux<Trade> findByInstrumentId(@NotNull @Min(1) Long instrumentId);

    // Fetch trades with price greater than specified value
    Flux<Trade> findByPriceGreaterThan(@NotNull @Min(0) Double price);

    // Fetch trades by timestamp range
    Flux<Trade> findByTimestampBetween(@NotNull Instant start, @NotNull Instant end);

    // Fetch all trades
    @Override
    @NonNull
    Flux<Trade> findAll();
}
