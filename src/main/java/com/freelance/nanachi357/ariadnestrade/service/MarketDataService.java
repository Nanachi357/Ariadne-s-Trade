package com.freelance.nanachi357.ariadnestrade.service;

import com.freelance.nanachi357.ariadnestrade.model.Trade;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import com.freelance.nanachi357.ariadnestrade.repository.MarketDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MarketDataService {

    private final MarketDataRepository marketDataRepository;

    public MarketDataService(MarketDataRepository marketDataRepository) {
        this.marketDataRepository = marketDataRepository;
    }

    // Retrieve all trades
    public List<Trade> getAllTrades() {
        return marketDataRepository.findAll();
    }

    // Retrieve trades by Instrument object
    public List<Trade> getTradesByInstrument(Instrument instrument) {
        return marketDataRepository.findByInstrument(instrument);
    }

    // Retrieve trades by Instrument ID
    public List<Trade> getTradesByInstrumentId(Long instrumentId) {
        return marketDataRepository.findByInstrument_Id(instrumentId);
    }

    // Retrieve trades with price greater than the specified value
    public List<Trade> getTradesByPriceGreaterThan(Double price) {
        return marketDataRepository.findByPriceGreaterThan(price);
    }

    // Retrieve trades that occurred between two timestamps
    public List<Trade> getTradesByTimestampRange(LocalDateTime start, LocalDateTime end) {
        return marketDataRepository.findByTimestampBetween(start, end);
    }

    // Save a trade to the database
    public Trade saveTrade(Trade trade) {
        return marketDataRepository.save(trade);
    }
}
