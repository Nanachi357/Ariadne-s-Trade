package com.freelance.nanachi357.ariadnestrade.api.controller;

import com.freelance.nanachi357.ariadnestrade.api.service.InstrumentService;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class InstrumentController {

    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping("/api/instrument/{instrumentName}")
    public Mono<Instrument> getInstrument(@PathVariable String instrumentName) {
        return instrumentService.fetchAndConvertInstrument(instrumentName);
    }

    @GetMapping("/api/instruments")
    public Mono<List<Instrument>> getInstruments(@RequestParam String currency,
                                                 @RequestParam(required = false) String kind,
                                                 @RequestParam(required = false, defaultValue = "false") boolean expired) {
        // Fetch and save instruments
        return instrumentService.fetchAndSaveInstruments(currency, kind, expired);
    }
}
