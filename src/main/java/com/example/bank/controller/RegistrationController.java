package com.example.bank.controller;

import com.example.bank.exception.MailException;
import com.example.bank.exception.PasswordException;
import com.example.bank.rest.UserInfoRequestDTO;
import com.example.bank.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    AuthService authService;

    @PostMapping()
    public ResponseEntity<?> register(@RequestBody UserInfoRequestDTO request) {
        try {
            authService.register(request);
        } catch (MailException | PasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Пользователь зарегистрирован!");
    }
}
