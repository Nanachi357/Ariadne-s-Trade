package com.freelance.nanachi357.ariadnestrade.api.service;

import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentDTO;
import com.freelance.nanachi357.ariadnestrade.api.converter.InstrumentConverter;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InstrumentService {

    private final InstrumentApiService instrumentApiService;
    private final InstrumentConverter converter;
    private static final Logger logger = LoggerFactory.getLogger(InstrumentService.class);

    public InstrumentService(InstrumentApiService instrumentApiService, InstrumentConverter converter) {
        this.instrumentApiService = instrumentApiService;
        this.converter = converter;
    }

    // Fetch instrument directly from the API
    @Cacheable(value = "instruments", key = "#instrumentName", unless = "#result == null")
    public Mono<InstrumentDTO> fetchInstrument(String instrumentName) {
        logger.info("Attempting to fetch instrument {} from cache", instrumentName);
        return instrumentApiService.fetchInstrument(instrumentName)
                .doOnNext(instrument -> logger.debug("Fetched instrument from API: {}", instrument))
                .onErrorResume(e -> {
                    logger.error("Error fetching instrument from API: {}", e.getMessage());
                    return Mono.empty(); // Return empty if not found or error occurs
                });
    }

    // Fetch all instruments based on parameters directly from API
    @Cacheable(value = "instruments", key = "{#currency, #kind, #expired}", unless = "#result.isEmpty()")
    public Flux<InstrumentDTO> fetchInstruments(String currency, String kind, boolean expired) {
        logger.info("Fetching instruments from API for currency: {}, kind: {}, expired: {}", currency, kind, expired);
        return instrumentApiService.fetchInstruments(currency, kind, expired)
                .doOnNext(instrument -> logger.debug("Fetched instrument from API: {}", instrument))
                .onErrorResume(e -> {
                    logger.error("Error fetching instruments from API: {}", e.getMessage());
                    return Flux.empty(); // Return empty if an error occurs
                });
    }

    // Evicting a specific instrument from the cache (if it was changed or deleted)
    @CacheEvict(value = "instruments", key = "#instrumentName")
    public void evictInstrumentFromCache(String instrumentName) {
        logger.info("Evicting instrument {} from cache", instrumentName);
    }
}
