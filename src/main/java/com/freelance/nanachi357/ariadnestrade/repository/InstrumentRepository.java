package com.freelance.nanachi357.ariadnestrade.repository;

import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    // Custom query method to find an instrument by its name
    Optional<Instrument> findByInstrumentName(String instrumentName);
    Optional<Instrument> findByBaseCurrency(String baseCurrency);
}
