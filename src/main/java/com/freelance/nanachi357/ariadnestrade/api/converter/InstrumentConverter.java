package com.freelance.nanachi357.ariadnestrade.api.converter;

import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentDTO;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InstrumentConverter {

    // Convert InstrumentDTO to Instrument entity
    public Instrument convertToInstrumentEntity(InstrumentDTO dto) {
        Instrument instrument = new Instrument();
        instrument.setInstrumentName(dto.getInstrumentName());
        instrument.setBaseCurrency(dto.getBaseCurrency());
        instrument.setQuoteCurrency(dto.getQuoteCurrency());
        instrument.setContractSize(dto.getContractSize());
        instrument.setIsActive(dto.getIsActive());
        instrument.setCreationTimestamp(convertLongToInstant(dto.getCreationTimestamp()));
        instrument.setExpirationTimestamp(convertLongToInstant(dto.getExpirationTimestamp()));
        instrument.setMakerCommission(dto.getMakerCommission());
        instrument.setTakerCommission(dto.getTakerCommission());
        instrument.setSettlementCurrency(dto.getSettlementCurrency());
        instrument.setMinTradeAmount(dto.getMinTradeAmount());
        // Add other fields if needed
        return instrument;
    }

    // Convert Instrument entity to InstrumentDTO
    public InstrumentDTO convertToInstrumentDTO(Instrument instrument) {
        InstrumentDTO dto = new InstrumentDTO();
        dto.setInstrumentName(instrument.getInstrumentName());
        dto.setBaseCurrency(instrument.getBaseCurrency());
        dto.setQuoteCurrency(instrument.getQuoteCurrency());
        dto.setContractSize(instrument.getContractSize());
        dto.setIsActive(instrument.getIsActive());
        dto.setCreationTimestamp(convertInstantToLong(instrument.getCreationTimestamp()));
        dto.setExpirationTimestamp(convertInstantToLong(instrument.getExpirationTimestamp()));
        dto.setMakerCommission(instrument.getMakerCommission());
        dto.setTakerCommission(instrument.getTakerCommission());
        dto.setSettlementCurrency(instrument.getSettlementCurrency());
        dto.setMinTradeAmount(instrument.getMinTradeAmount());
        // Add other fields if needed
        return dto;
    }

    // Helper method to convert Long timestamp to Instant
    private Instant convertLongToInstant(Long timestamp) {
        return (timestamp != null) ? Instant.ofEpochMilli(timestamp) : null;
    }

    // Helper method to convert Instant to Long timestamp
    private Long convertInstantToLong(Instant instant) {
        return (instant != null) ? instant.toEpochMilli() : null;
    }
}
