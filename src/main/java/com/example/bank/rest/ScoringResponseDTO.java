package com.example.bank.rest;

import com.example.bank.entity.CreditTariff;
import com.example.bank.entity.Scoring;
import com.example.bank.entity.User;
import lombok.Data;


@Data
public class ScoringResponseDTO {
    private Long id;

    private Long creditTariffId;

    private Long userId;

    private Long term;

    private Long amount;

    private Double rate;

    private java.sql.Timestamp applicationDate;

    private boolean approved;

    public static ScoringResponseDTO fromScoring(Scoring scoring) {
        ScoringResponseDTO response = new ScoringResponseDTO();
        response.setId(scoring.getId());
        response.setCreditTariffId(scoring.getCreditTariff().getId());
        response.setUserId(scoring.getUser().getId());
        response.setTerm(scoring.getTerm());
        response.setAmount(scoring.getAmount());
        response.setApplicationDate(scoring.getApplicationDate());
        response.setApproved(scoring.isApproved());
        return response;
    }
}
