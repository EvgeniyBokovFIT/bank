package com.example.bank.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserProfileCreditResponse {
    private Long amount;

    private Double rate;

    private java.sql.Timestamp startDate;

    private java.sql.Timestamp endDate;

    public UserProfileCreditResponse(long amount, double rate, Date startDate, Date endDate) {
        this.amount = amount;
        this.rate = rate;
        this.startDate = new Timestamp(startDate.getTime());
        this.endDate = new Timestamp(endDate.getTime());
    }
}
