package com.freelance.nanachi357.ariadnestrade.api;

import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentDTO;
import com.freelance.Nanachi357.DeribitJavaConnector.dto.TradeDTO;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import com.freelance.nanachi357.ariadnestrade.model.Trade;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class ApiToEntityConverter {

    // Convert InstrumentDTO to Instrument entity
    public Instrument convertToInstrumentEntity(InstrumentDTO dto) {
        Instrument instrument = new Instrument();
        instrument.setInstrumentName(dto.getInstrumentName());
        instrument.setBaseCurrency(dto.getBaseCurrency());
        instrument.setQuoteCurrency(dto.getQuoteCurrency());
        instrument.setContractSize(dto.getContractSize());
        instrument.setIsActive(dto.getIsActive());
        instrument.setCreationTimestamp(convertLongToLocalDateTime(dto.getCreationTimestamp()));
        instrument.setExpirationTimestamp(convertLongToLocalDateTime(dto.getExpirationTimestamp()));
        return instrument;
    }

    // Convert TradeDTO to Trade entity and associate it with an Instrument
    public Trade convertToTradeEntity(TradeDTO dto, Instrument instrument) {
        Trade trade = new Trade();
        trade.setTradeSeq(dto.getTradeSeq());
        trade.setTradeId(dto.getTradeId());
        trade.setTimestamp(convertLongToLocalDateTime(dto.getTimestamp()));
        trade.setPrice(dto.getPrice());
        trade.setAmount(dto.getAmount());
        trade.setDirection(dto.getDirection());
        trade.setInstrument(instrument); // Associate the trade with the instrument
        return trade;
    }

    // Helper method to convert Long timestamp to LocalDateTime
    private LocalDateTime convertLongToLocalDateTime(Long timestamp) {
        return (timestamp != null) ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()) : null;
    }
}
