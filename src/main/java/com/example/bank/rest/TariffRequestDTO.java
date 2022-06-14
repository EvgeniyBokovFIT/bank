package com.example.bank.rest;

import lombok.Data;

@Data
public class TariffRequestDTO {
    private Long id;

    private Long minTerm;

    private Long maxTerm;

    private Long minAmount;

    private Long maxAmount;

    private Double minRate;

    private Double maxRate;
}
