package com.freelance.nanachi357.ariadnestrade.api.service;

import com.freelance.Nanachi357.DeribitJavaConnector.service.GetLastTradesByCurrencyAndTime;
import com.freelance.nanachi357.ariadnestrade.api.ApiToEntityConverter;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import com.freelance.nanachi357.ariadnestrade.model.Trade;
import com.freelance.nanachi357.ariadnestrade.repository.TradeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    private final GetLastTradesByCurrencyAndTime getLastTradesByCurrencyAndTime;
    private final ApiToEntityConverter converter;
    private final TradeRepository tradeRepository;
    private final InstrumentService instrumentService;

    public TradeService(GetLastTradesByCurrencyAndTime getLastTradesByCurrencyAndTime,
                        ApiToEntityConverter converter,
                        TradeRepository tradeRepository,
                        InstrumentService instrumentService) {
        this.getLastTradesByCurrencyAndTime = getLastTradesByCurrencyAndTime;
        this.converter = converter;
        this.tradeRepository = tradeRepository;
        this.instrumentService = instrumentService;
    }

    public Mono<List<Trade>> fetchAndSaveTrades(String currency, long startTimestamp, long endTimestamp, int count) {
        return instrumentService.fetchAndSaveInstrumentIfNotFound(currency) // Use InstrumentService
                .flatMap(instrument -> fetchTradesFromApiAndSave(currency, startTimestamp, endTimestamp, count, instrument))
                .onErrorResume(e -> {
                    // Log error and return empty Mono
                    System.err.println("Error fetching trades: " + e.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<List<Trade>> fetchTradesFromApiAndSave(String currency, long startTimestamp, long endTimestamp, int count, Instrument instrument) {
        return getLastTradesByCurrencyAndTime.fetchLastTrades(currency, startTimestamp, endTimestamp, count)
                .doOnNext(tradesResponseDTO -> {
                    System.out.println("API Response: " + tradesResponseDTO);
                    if (tradesResponseDTO.getResult().getTrades().isEmpty()) {
                        System.out.println("No trades found in the API response");
                    }
                })
                .flatMap(tradesResponseDTO -> {
                    // Convert DTO to entity and save to repository
                    List<Trade> trades = tradesResponseDTO.getResult().getTrades().stream()
                            .map(tradeDTO -> converter.convertToTradeEntity(tradeDTO, instrument)) // Convert DTO to entity
                            .collect(Collectors.toList());

                    // Save trades
                    tradeRepository.saveAll(trades);
                    return Mono.just(trades);
                });
    }
}
