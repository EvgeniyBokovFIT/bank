package com.example.bank.rest;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PaymentRequestDTO {
    private Long creditId;
    private Timestamp date;
    private Long amount;
    private Long paymentMethodId;
}
