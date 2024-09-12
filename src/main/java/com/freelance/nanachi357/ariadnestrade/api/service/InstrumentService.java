package com.freelance.nanachi357.ariadnestrade.api.service;

import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentDTO;
import com.freelance.Nanachi357.DeribitJavaConnector.dto.InstrumentsResponseDTO;
import com.freelance.Nanachi357.DeribitJavaConnector.service.GetInstrument;
import com.freelance.nanachi357.ariadnestrade.api.ApiToEntityConverter;
import com.freelance.nanachi357.ariadnestrade.model.Instrument;
import com.freelance.nanachi357.ariadnestrade.repository.InstrumentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class InstrumentService {

    private final GetInstrument getInstrument;
    private final ApiToEntityConverter converter;
    private final InstrumentRepository instrumentRepository;


    public InstrumentService(GetInstrument getInstrument, ApiToEntityConverter converter, InstrumentRepository instrumentRepository) {
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

    public Mono<Instrument> fetchAndSaveInstrumentIfNotFound(String instrumentName) {
        // First, check if the instrument is already in the database
        return Mono.justOrEmpty(instrumentRepository.findByInstrumentName(instrumentName))
                // If it's not found, call the Deribit API
                .switchIfEmpty(
                        getInstrument.fetchInstrument(instrumentName)
                                .flatMap(instrumentDTO -> {
                                    // Convert the DTO to an entity and save it in the database
                                    Instrument instrument = converter.convertToInstrumentEntity(instrumentDTO.getResult());
                                    instrumentRepository.save(instrument);
                                    return Mono.just(instrument);
                                })
                )
                .onErrorResume(e -> {
                    // Handle errors gracefully, log them
                    System.err.println("Error fetching instrument: " + e.getMessage());
                    return Mono.empty();
                });
    }

    // Method to handle the response from the API and convert DTO to entity
    private Instrument handleInstrumentResponse(InstrumentsResponseDTO responseDTO) {
        if (responseDTO == null || responseDTO.getResult() == null) {
            throw new RuntimeException("Instrument data not found in response.");
        }

        InstrumentDTO instrumentDTO = responseDTO.getResult();
        // Convert the response DTO to the database entity using the converter
        return converter.convertToInstrumentEntity(instrumentDTO);
    }
}
