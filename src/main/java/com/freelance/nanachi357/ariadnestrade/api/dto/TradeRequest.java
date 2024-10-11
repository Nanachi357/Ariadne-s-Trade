package com.freelance.nanachi357.ariadnestrade.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TradeRequest {

    private String currency;
    private long startTimestamp;
    private long endTimestamp;
    private int count;

    // Getters and Setters

}
