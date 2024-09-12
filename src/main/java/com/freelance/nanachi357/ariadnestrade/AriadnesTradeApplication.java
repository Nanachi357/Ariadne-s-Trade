package com.freelance.nanachi357.ariadnestrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.freelance.nanachi357.ariadnestrade", "com.freelance.Nanachi357.DeribitJavaConnector"})
public class AriadnesTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AriadnesTradeApplication.class, args);
    }

}
