package com.freelance.nanachi357.ariadnestrade.repository;

import com.freelance.nanachi357.ariadnestrade.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByInstrument_Id(Long instrumentId); // Custom method to find trades by instrument ID
}
