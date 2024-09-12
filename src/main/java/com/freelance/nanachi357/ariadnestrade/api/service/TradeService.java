package com.freelance.nanachi357.ariadnestrade.api.service;

import com.freelance.Nanachi357.DeribitJavaConnector.service.GetLastTradesByCurrencyAndTime;
import com.freelance.nanachi357.ariadnestrade.api.ApiToEntityConverter;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import com.freelance.nanachi357.ariadnestrade.model.Trade;
import com.freelance.nanachi357.ariadnestrade.repository.InstrumentRepository;
import com.freelance.nanachi357.ariadnestrade.repository.TradeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    private final GetLastTradesByCurrencyAndTime getLastTradesByCurrencyAndTime;
    private final ApiToEntityConverter converter;
    private final TradeRepository tradeRepository;
    private final InstrumentRepository instrumentRepository;

    public TradeService(GetLastTradesByCurrencyAndTime getLastTradesByCurrencyAndTime,
                        ApiToEntityConverter converter,
                        TradeRepository tradeRepository,
                        InstrumentRepository instrumentRepository) {
        this.getLastTradesByCurrencyAndTime = getLastTradesByCurrencyAndTime;
        this.converter = converter;
        this.tradeRepository = tradeRepository;
        this.instrumentRepository = instrumentRepository;
    }

    public Mono<List<Trade>> fetchAndSaveTrades(String currency, long startTimestamp, long endTimestamp, int count) {
        // Offload the blocking call to a boundedElastic scheduler
        return Mono.fromCallable(() -> instrumentRepository.findByInstrumentName(currency))
                .subscribeOn(Schedulers.boundedElastic())  // Switch to a scheduler for blocking calls
                .flatMap(optionalInstrument -> {
                    if (optionalInstrument.isEmpty()) {
                        return Mono.error(new RuntimeException("Instrument not found: " + currency));
                    }
                    Instrument instrument = optionalInstrument.get();
                    return fetchTradesFromApiAndSave(currency, startTimestamp, endTimestamp, count, instrument);
                })
                .onErrorResume(e -> {
                    // Log error and return empty Mono
                    System.err.println("Error fetching trades: " + e.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<List<Trade>> fetchTradesFromApiAndSave(String currency, long startTimestamp, long endTimestamp, int count, Instrument instrument) {
        return getLastTradesByCurrencyAndTime.fetchLastTrades(currency, startTimestamp, endTimestamp, count)
                .map(tradesResponseDTO -> {
                    // Extract trades from the API response
                    List<Trade> trades = tradesResponseDTO.getResult().getTrades().stream()
                            .map(tradeDTO -> converter.convertToTradeEntity(tradeDTO, instrument)) // Convert DTO to entity
                            .collect(Collectors.toList());

                    // Save all trades to the repository
                    tradeRepository.saveAll(trades);

                    return trades; // Return the saved trades
                });
    }
}
