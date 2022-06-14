package com.example.bank.controller;

import com.example.bank.exception.PasswordException;
import com.example.bank.exception.RecoveryCodeException;
import com.example.bank.rest.ForgotPasswordDTO;
import com.example.bank.rest.PasswordRecoveryDTO;
import com.example.bank.service.PasswordRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/auth/password-recovery")
public class PasswordRecoveryController {

    @Autowired
    PasswordRecoveryService passwordRecoveryService;

    private String mail;

    @GetMapping()
    public ResponseEntity<?> sendMessageWithCode(@RequestBody ForgotPasswordDTO request) {
        this.mail = request.getMail();
        try {
            passwordRecoveryService.sendMailToPasswordRecovery(mail);
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Письмо отправлено");
    }

    @PostMapping()
    public ResponseEntity<?> recoverPassword(@RequestBody PasswordRecoveryDTO request) {
        try {
            passwordRecoveryService.recoverPassword(request);
        } catch (PasswordException | RecoveryCodeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Пароль обновлен");
    }
}
