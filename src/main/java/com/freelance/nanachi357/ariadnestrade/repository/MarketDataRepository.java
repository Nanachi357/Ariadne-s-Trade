package com.freelance.nanachi357.ariadnestrade.repository;

import com.freelance.nanachi357.ariadnestrade.model.Trade;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MarketDataRepository extends JpaRepository<Trade, Long> {

    @NonNull
    // Fetch trades by Instrument
    List<Trade> findByInstrument(Instrument instrument);

    @NonNull
    // Fetch trades by instrument ID
    List<Trade> findByInstrument_Id(Long instrumentId);

    @NonNull
    // Fetch trades with price greater than specified value
    List<Trade> findByPriceGreaterThan(Double price);

    @NonNull
    // Fetch trades by timestamp range
    List<Trade> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @NonNull
    @Override
    // Fetch all trades
    List<Trade> findAll();
}
