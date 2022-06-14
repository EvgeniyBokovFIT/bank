package com.example.bank.rest;

import lombok.Data;

@Data
public class BanUserRequestDTO {
    private Long id;

    private String mail;

    private String firstName;

    private String lastName;

    private String passportData;

    private Long amountOfDays;
}
