package com.example.bank.controller;

import com.example.bank.rest.PaymentRequestDTO;
import com.example.bank.service.AnnuityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/admin/payment")
public class PaymentController {

    @Autowired
    AnnuityService annuityService;

    @PostMapping()
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequestDTO request) {
        annuityService.handlePayment(request);
        return ResponseEntity.ok("Платеж внесен");
    }
}
