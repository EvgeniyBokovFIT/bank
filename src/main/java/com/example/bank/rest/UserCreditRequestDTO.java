package com.example.bank.rest;

import lombok.Data;

@Data
public class UserCreditRequestDTO {

    private java.sql.Timestamp creditMinStartDate;

    private java.sql.Timestamp creditMaxStartDate;

    private java.sql.Timestamp creditMinEndDate;

    private java.sql.Timestamp creditMaxEndDate;

    private Long creditMinAmount;

    private Long creditMaxAmount;

    private String firstName;

    private String lastName;

}
