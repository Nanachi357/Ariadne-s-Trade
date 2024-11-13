package com.freelance.nanachi357.ariadnestrade.api.controller;

import com.freelance.nanachi357.ariadnestrade.api.service.InstrumentService;
import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@RestController
@Validated
public class InstrumentController {

    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping("/api/instrument/{instrumentName}")
    public Mono<InstrumentDTO> getInstrument(@PathVariable @NotBlank(message = "Instrument name cannot be blank") String instrumentName) {
        return instrumentService.fetchInstrument(instrumentName);
    }

    @GetMapping("/api/instruments")
    public Flux<InstrumentDTO> getInstruments(
            @RequestParam @NotBlank(message = "Currency cannot be blank")
            @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid ISO currency code") String currency,
            @RequestParam(required = false) String kind,
            @RequestParam(required = false, defaultValue = "false") boolean expired) {
        return instrumentService.fetchInstruments(currency, kind, expired);
    }

    // Method to evict an instrument from the cache
    @DeleteMapping("/api/instrument/{instrumentName}")
    public Mono<Void> evictInstrument(@PathVariable @NotBlank(message = "Instrument name cannot be blank") String instrumentName) {
        instrumentService.evictInstrumentFromCache(instrumentName); // Clear cache for the specific instrument
        return Mono.empty(); // No need for further logic since we're not deleting from a data source
    }
}
