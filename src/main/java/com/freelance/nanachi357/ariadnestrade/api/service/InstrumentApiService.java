package com.freelance.nanachi357.ariadnestrade.api.service;

import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentDTO;
import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentResponseDTO;
import com.freelance.Nanachi357.DeribitJavaConnector.service.GetInstrument;
import com.freelance.Nanachi357.DeribitJavaConnector.service.GetInstruments;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InstrumentApiService {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentApiService.class);

    private final GetInstruments getInstruments;
    private final GetInstrument getInstrument;

    public InstrumentApiService(GetInstruments getInstruments, GetInstrument getInstrument) {
        this.getInstruments = getInstruments;
        this.getInstrument = getInstrument;
    }

    // Method to fetch a specific instrument from the API
    public Mono<InstrumentDTO> fetchInstrument(String instrumentName) {
        logger.info("Fetching instrument from Deribit API for instrument name: {}", instrumentName);
        return getInstrument.fetchInstrument(instrumentName)
                .map(InstrumentResponseDTO::getResult)
                .onErrorResume(e -> {
                    logger.error("Error fetching instrument from Deribit API: {}", e.getMessage());
                    return Mono.error(new RuntimeException("Failed to fetch instrument: " + instrumentName, e));
                });
    }

    // Method to fetch instruments from the API based on currency, kind, and expiration status
    public Flux<InstrumentDTO> fetchInstruments(String currency, String kind, boolean expired) {
        logger.info("Fetching instruments from Deribit API for currency: {}, kind: {}, expired: {}", currency, kind, expired);
        return getInstruments.fetchInstruments(currency, kind, expired)
                .flatMapMany(Flux::fromIterable)
                .onErrorResume(e -> {
                    logger.error("Error fetching instruments from Deribit API: {}", e.getMessage());
                    return Flux.error(new RuntimeException("Failed to fetch instruments for currency: " + currency, e));
                });
    }
}
