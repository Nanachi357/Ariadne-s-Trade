package com.freelance.nanachi357.ariadnestrade.api.controller;

import com.freelance.nanachi357.ariadnestrade.api.service.TradeService;
import com.freelance.nanachi357.ariadnestrade.model.Trade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/api/trades")
    public Mono<List<Trade>> getTrades(@RequestParam String currency,
                                       @RequestParam long startTimestamp,
                                       @RequestParam long endTimestamp,
                                       @RequestParam int count) {
        return tradeService.fetchAndSaveTrades(currency, startTimestamp, endTimestamp, count)
                .switchIfEmpty(Mono.error(new RuntimeException("No trades found for " + currency)))
                .onErrorResume(e -> {
                    System.err.println("Error fetching trades: " + e.getMessage());
                    return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()));
                });
    }
}
