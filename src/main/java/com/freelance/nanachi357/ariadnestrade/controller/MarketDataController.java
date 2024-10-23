package com.freelance.nanachi357.ariadnestrade.controller;

import com.freelance.nanachi357.ariadnestrade.model.Trade;
import com.freelance.nanachi357.ariadnestrade.service.MarketDataService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/api/marketdata")
public class MarketDataController {

    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    // Retrieve all trades
    @GetMapping("/trades")
    public Flux<Trade> getAllTrades() {
        return marketDataService.getAllTrades();
    }

    // Retrieve trades by Instrument ID
    @GetMapping("/trades/instrument/{instrumentId}")
    public Flux<Trade> getTradesByInstrumentId(@PathVariable Long instrumentId) {
        return marketDataService.getTradesByInstrumentId(instrumentId)
                .onErrorResume(e -> Flux.error(new RuntimeException("Error fetching trades by instrument ID: " + e.getMessage())));
    }

    // Retrieve trades with price greater than the specified value
    @GetMapping("/trades/price")
    public Flux<Trade> getTradesByPriceGreaterThan(@RequestParam Double price) {
        return marketDataService.getTradesByPriceGreaterThan(price)
                .onErrorResume(e -> Flux.error(new RuntimeException("Error fetching trades by price: " + e.getMessage())));
    }

    // Retrieve trades by timestamp range
    @GetMapping("/trades/timestamp")
    public Flux<Trade> getTradesByTimestampRange(@RequestParam Instant start,
                                                 @RequestParam Instant end) {
        return marketDataService.getTradesByTimestampRange(start, end)
                .onErrorResume(e -> Flux.error(new RuntimeException("Error fetching trades by timestamp range: " + e.getMessage())));
    }

    // Save a new trade
    @PostMapping("/trades")
    public Mono<Trade> saveTrade(@RequestBody Trade trade) {
        return marketDataService.saveTrade(trade)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error saving trade: " + e.getMessage())));
    }
}
