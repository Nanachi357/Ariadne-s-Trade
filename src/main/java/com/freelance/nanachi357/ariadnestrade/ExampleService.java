package com.freelance.nanachi357.ariadnestrade;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    private final ExampleRepository exampleRepository;

    public ExampleService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @PostConstruct
    public void init() {
        // Create a new ExampleEntity and save it to the database
        ExampleEntity entity = new ExampleEntity();
        entity.setName("Test Entity");
        exampleRepository.save(entity);

        // Retrieve and print all entities from the database
        exampleRepository.findAll().forEach(e -> System.out.println(e.getName()));
    }
}
