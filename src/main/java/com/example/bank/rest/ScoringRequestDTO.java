package com.example.bank.rest;

import lombok.Data;

@Data
public class ScoringRequestDTO {

    private Long id;

    private Long tariffId;

    private Long userId;

    private Long term;

    private Long amount;

    private Double rate;

    private boolean approved;
}
