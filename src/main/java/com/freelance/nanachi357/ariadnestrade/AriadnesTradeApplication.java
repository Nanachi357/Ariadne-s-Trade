package com.freelance.nanachi357.ariadnestrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = {"com.freelance.nanachi357.ariadnestrade", "com.freelance.Nanachi357.DeribitJavaConnector"})
@EnableR2dbcRepositories(basePackages = "com.freelance.nanachi357.ariadnestrade.repository")
public class AriadnesTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AriadnesTradeApplication.class, args);
    }

}
