package com.freelance.nanachi357.ariadnestrade.api.service;

import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentDTO;
import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentResponseDTO;
import com.freelance.Nanachi357.DeribitJavaConnector.service.GetInstrument;
import com.freelance.Nanachi357.DeribitJavaConnector.service.GetInstruments;
import com.freelance.nanachi357.ariadnestrade.api.ApiToEntityConverter;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import com.freelance.nanachi357.ariadnestrade.repository.InstrumentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class InstrumentService {

    private final GetInstruments getInstruments;
    private final GetInstrument getInstrument;
    private final ApiToEntityConverter converter;
    private final InstrumentRepository instrumentRepository;


    public InstrumentService(GetInstruments getInstruments, GetInstrument getInstrument, ApiToEntityConverter converter, InstrumentRepository instrumentRepository) {
        this.getInstruments = getInstruments;
        this.getInstrument = getInstrument;
        this.converter = converter;
        this.instrumentRepository = instrumentRepository;
    }

    public Mono<Instrument> fetchAndConvertInstrument(String instrumentName) {
        return getInstrument.fetchInstrument(instrumentName)
                .map(this::handleInstrumentResponse)
                .onErrorResume(e -> {
                    // Log the error and return empty Mono
                    System.err.println("Error fetching instrument from Deribit API: " + e.getMessage());
                    return Mono.empty(); // Returning empty Mono in case of error
                });
    }

    public Mono<Instrument> fetchAndSaveInstrumentIfNotFound(String currency) {
        // Log before fetching from DB
        System.out.println("Looking for instrument in the database: " + currency);

        return Mono.fromCallable(() -> instrumentRepository.findByBaseCurrency(currency))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalInstrument -> {
                    if (optionalInstrument.isPresent()) {
                        System.out.println("Instrument found in database: " + optionalInstrument.get());
                        return Mono.just(optionalInstrument.get());
                    }
                    // Log before fetching from API
                    System.out.println("Instrument not found, fetching from Deribit API: " + currency);
                    return getInstrument.fetchInstrument(currency)
                            .flatMap(instrumentDTO -> {
                                // Log before saving
                                System.out.println("Fetched from API: " + instrumentDTO.getResult());
                                Instrument instrument = converter.convertToInstrumentEntity(instrumentDTO.getResult());

                                // Save the instrument in a blocking context using boundedElastic scheduler
                                return Mono.fromCallable(() -> instrumentRepository.save(instrument))
                                        .subscribeOn(Schedulers.boundedElastic())  // Offload blocking call
                                        .thenReturn(instrument); // Return the saved instrument
                            });
                })
                .onErrorResume(e -> {
                    // Handle errors gracefully
                    System.err.println("Error fetching instrument: " + e.getMessage());
                    return Mono.empty();
                });
    }

    public Mono<List<Instrument>> fetchAndSaveInstruments(String currency, String kind, boolean expired) {
        // Fetch instruments from Deribit API
        System.out.println("Fetching and saving instruments for currency: " + currency + ", kind: " + kind);
        return getInstruments.fetchInstruments(currency, kind, expired)
                .flatMapMany(Flux::fromIterable)
                .flatMap(instrumentDTO -> {
                    // Convert DTO to entity
                    Instrument instrument = converter.convertToInstrumentEntity(instrumentDTO);

                    // Save if not present in the database
                    return Mono.fromCallable(() -> {
                        if (!instrumentRepository.existsByInstrumentName(instrument.getInstrumentName())) {
                            instrumentRepository.save(instrument);
                            System.out.println("Saved instrument: " + instrument.getInstrumentName());
                        }
                        return instrument;
                    }).subscribeOn(Schedulers.boundedElastic());
                })
                .collectList()
                .onErrorResume(e -> {
                    // Handle errors gracefully
                    System.err.println("Error fetching or saving instruments: " + e.getMessage());
                    return Mono.empty();
                });
    }

    // Method to handle the response from the API and convert DTO to entity
    private Instrument handleInstrumentResponse(InstrumentResponseDTO responseDTO) {
        if (responseDTO == null || responseDTO.getResult() == null) {
            throw new RuntimeException("Instrument data not found in response.");
        }

        InstrumentDTO instrumentDTO = responseDTO.getResult();
        // Convert the response DTO to the database entity using the converter
        return converter.convertToInstrumentEntity(instrumentDTO);
    }
}
