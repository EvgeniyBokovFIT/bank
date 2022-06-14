package com.example.bank.rest;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class UserCreditResponseDTO {

    private String firstName;

    private String lastName;

    private Long term;

    private Double rate;

    private Long creditAmount;

    private java.sql.Timestamp startDate;

    private java.sql.Timestamp endDate;

    public UserCreditResponseDTO(String firstName, String lastName, long term, double rate, long creditAmount, Date startDate, Date endDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.term = term;
        this.rate = rate;
        this.creditAmount = creditAmount;
        this.startDate = new Timestamp(startDate.getTime());
        this.endDate = new Timestamp(endDate.getTime());
    }

}
