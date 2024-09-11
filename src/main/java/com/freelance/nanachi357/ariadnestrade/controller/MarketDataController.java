package com.freelance.nanachi357.ariadnestrade.controller;

import com.freelance.nanachi357.ariadnestrade.model.Trade;
import com.freelance.nanachi357.ariadnestrade.service.MarketDataService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/marketdata")
public class MarketDataController {

    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    // Retrieve all trades
    @GetMapping("/trades")
    public List<Trade> getAllTrades() {
        return marketDataService.getAllTrades();
    }

    // Retrieve trades by Instrument ID
    @GetMapping("/trades/instrument/{instrumentId}")
    public List<Trade> getTradesByInstrumentId(@PathVariable Long instrumentId) {
        return marketDataService.getTradesByInstrumentId(instrumentId);
    }

    // Retrieve trades with price greater than the specified value
    @GetMapping("/trades/price")
    public List<Trade> getTradesByPriceGreaterThan(@RequestParam Double price) {
        return marketDataService.getTradesByPriceGreaterThan(price);
    }

    // Retrieve trades by timestamp range
    @GetMapping("/trades/timestamp")
    public List<Trade> getTradesByTimestampRange(@RequestParam LocalDateTime start,
                                                 @RequestParam LocalDateTime end) {
        return marketDataService.getTradesByTimestampRange(start, end);
    }

    // Save a new trade
    @PostMapping("/trades")
    public Trade saveTrade(@RequestBody Trade trade) {
        return marketDataService.saveTrade(trade);
    }
}
