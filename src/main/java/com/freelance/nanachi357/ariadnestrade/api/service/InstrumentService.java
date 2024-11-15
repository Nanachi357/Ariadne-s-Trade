package com.freelance.nanachi357.ariadnestrade.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentDTO;
import com.freelance.nanachi357.ariadnestrade.api.converter.InstrumentConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Service
public class InstrumentService {

    private final InstrumentApiService instrumentApiService;
    private final InstrumentConverter converter;
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(InstrumentService.class);

    public InstrumentService(
            InstrumentApiService instrumentApiService,
            InstrumentConverter converter,
            @Qualifier("customReactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
        this.instrumentApiService = instrumentApiService;
        this.converter = converter;
        this.redisTemplate = redisTemplate;
    }

    // Fetch a single instrument from Redis or API
    public Mono<InstrumentDTO> fetchInstrument(String instrumentName) {
        String cacheKey = "instrument:" + instrumentName;
        logger.info("Attempting to fetch instrument '{}' from cache.", instrumentName);

        return redisTemplate.opsForValue()
                .get(cacheKey)
                .flatMap(json -> {
                    try {
                        logger.info("Cache hit for instrument '{}'.", instrumentName);
                        logRemainingTTL(cacheKey);
                        return Mono.just(converter.convertFromJson(json));
                    } catch (JsonProcessingException e) {
                        logger.error("Failed to deserialize instrument '{}' from cache: {}", instrumentName, e.getMessage());
                        return Mono.empty();
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.info("Cache miss for instrument '{}'. Fetching from API.", instrumentName);
                    return instrumentApiService.fetchInstrument(instrumentName)
                            .flatMap(instrument -> {
                                try {
                                    String json = converter.convertToJson(instrument);
                                    return redisTemplate.opsForValue()
                                            .set(cacheKey, json, Duration.ofMinutes(5))
                                            .thenReturn(instrument);
                                } catch (JsonProcessingException e) {
                                    logger.error("Failed to serialize instrument '{}' for caching: {}", instrumentName, e.getMessage());
                                    return Mono.just(instrument);
                                }
                            });
                }));
    }

    // Fetch multiple instruments from Redis or API
    public Flux<InstrumentDTO> fetchInstruments(String currency, String kind, boolean expired) {
        String cacheKey = String.format("instruments:%s:%s:%s", currency, kind, expired);
        logger.info("Attempting to fetch instruments for currency '{}', kind '{}', expired '{}' from cache.", currency, kind, expired);

        return redisTemplate.opsForValue()
                .get(cacheKey)
                .flatMapMany(json -> {
                    try {
                        logger.info("Cache hit for instruments with currency '{}', kind '{}', expired '{}'.", currency, kind, expired);
                        logRemainingTTL(cacheKey);
                        return Flux.fromIterable(converter.convertFromJsonArray(json));
                    } catch (JsonProcessingException e) {
                        logger.error("Failed to deserialize instruments for cache key '{}': {}", cacheKey, e.getMessage());
                        return Flux.empty();
                    }
                })
                .switchIfEmpty(Flux.defer(() -> {
                    logger.info("Cache miss for instruments with currency '{}', kind '{}', expired '{}'. Fetching from API.", currency, kind, expired);
                    return instrumentApiService.fetchInstruments(currency, kind, expired)
                            .collectList()
                            .flatMapMany(instruments -> {
                                try {
                                    String json = converter.convertToJsonArray(instruments);
                                    return redisTemplate.opsForValue()
                                            .set(cacheKey, json, Duration.ofMinutes(5))
                                            .thenMany(Flux.fromIterable(instruments));
                                } catch (JsonProcessingException e) {
                                    logger.error("Failed to serialize instruments for caching: {}", e.getMessage());
                                    return Flux.fromIterable(instruments);
                                }
                            });
                }));
    }

    // Evict a specific instrument from cache
    public Mono<Void> evictInstrumentFromCache(String instrumentName) {
        String cacheKey = "instrument:" + instrumentName;
        logger.warn("Evicting instrument '{}' from cache.", instrumentName);
        return redisTemplate.delete(cacheKey).then();
    }

    // Evict multiple instruments from cache
    public Mono<Void> evictInstrumentsFromCache(String currency, String kind, boolean expired) {
        String cacheKey = String.format("instruments:%s:%s:%s", currency, kind, expired);
        logger.warn("Evicting instruments for currency '{}', kind '{}', expired '{}' from cache.", currency, kind, expired);
        return redisTemplate.delete(cacheKey).then();
    }

    // Log the remaining TTL of a cache key
    private void logRemainingTTL(String cacheKey) {
        redisTemplate.getExpire(cacheKey).subscribe(ttl -> {
            if (ttl != null) {
                logger.info("Remaining TTL for key '{}': {} seconds", cacheKey, ttl.getSeconds());
            } else {
                logger.warn("Could not determine TTL for key '{}'.", cacheKey);
            }
        });
    }
}
