package com.example.bank.rest;

import lombok.Data;

@Data
public class UserInfoRequestDTO {
    private String mail;
    private String firstName;
    private String lastName;
    private String passportData;
    private String password;
    private String passwordConfirm;
}
