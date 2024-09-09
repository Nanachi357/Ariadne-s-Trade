package com.freelance.nanachi357.ariadnestrade.repository;

import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    // You can add custom queries if needed, or rely on default JpaRepository methods
}
