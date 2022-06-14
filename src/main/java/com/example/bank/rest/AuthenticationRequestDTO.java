package com.example.bank.rest;

import lombok.Data;

@Data
public class AuthenticationRequestDTO {
    private String mail;
    private String password;
}