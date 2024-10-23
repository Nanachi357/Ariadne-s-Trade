package com.freelance.nanachi357.ariadnestrade.repository;

import com.freelance.nanachi357.ariadnestrade.model.Trade;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import org.springframework.lang.NonNull;

@Repository
public interface TradeRepository extends R2dbcRepository<Trade, Long> {

    // Custom method to find trades by instrument ID
    @NonNull
    Flux<Trade> findByInstrumentId(Long instrumentId);
}
