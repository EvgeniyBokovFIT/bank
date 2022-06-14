package com.example.bank.rest;

import lombok.Data;

@Data
public class PasswordRecoveryDTO {
    private String mail;

    private String recoveryCode;

    private String newPassword;

    private String newPasswordConfirm;
}
