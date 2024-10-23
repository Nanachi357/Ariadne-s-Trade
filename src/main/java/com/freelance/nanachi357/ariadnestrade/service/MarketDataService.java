package com.freelance.nanachi357.ariadnestrade.service;

import com.freelance.nanachi357.ariadnestrade.model.Trade;
import com.freelance.nanachi357.ariadnestrade.repository.MarketDataRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class MarketDataService {

    private final MarketDataRepository marketDataRepository;

    public MarketDataService(MarketDataRepository marketDataRepository) {
        this.marketDataRepository = marketDataRepository;
    }

    // Retrieve all trades
    public Flux<Trade> getAllTrades() {
        return marketDataRepository.findAll();
    }

    // Retrieve trades by Instrument ID
    public Flux<Trade> getTradesByInstrumentId(Long instrumentId) {
        if (instrumentId == null || instrumentId <= 0) {
            return Flux.error(new IllegalArgumentException("Instrument ID must be greater than 0"));
        }
        return marketDataRepository.findByInstrumentId(instrumentId);
    }

    // Retrieve trades with price greater than the specified value
    public Flux<Trade> getTradesByPriceGreaterThan(Double price) {
        if (price == null || price < 0) {
            return Flux.error(new IllegalArgumentException("Price must be greater than or equal to 0"));
        }
        return marketDataRepository.findByPriceGreaterThan(price);
    }

    // Retrieve trades that occurred between two timestamps
    public Flux<Trade> getTradesByTimestampRange(Instant start, Instant end) {
        if (start == null || end == null) {
            return Flux.error(new IllegalArgumentException("Start and end timestamps cannot be null"));
        }
        return marketDataRepository.findByTimestampBetween(start, end);
    }

    // Save a trade to the database
    public Mono<Trade> saveTrade(Trade trade) {
        if (trade == null) {
            return Mono.error(new IllegalArgumentException("Trade cannot be null"));
        }
        return marketDataRepository.save(trade);
    }
}
