package com.example.bank.controller;

import com.example.bank.exception.CreditException;
import com.example.bank.rest.AnnuityRequestDTO;
import com.example.bank.rest.PaymentRequestDTO;
import com.example.bank.service.AnnuityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/admin/annuity")
public class AnnuityController {

    @Autowired
    AnnuityService annuityService;

    @PostMapping()
    public ResponseEntity<?> createAnnuity(@RequestBody AnnuityRequestDTO request) {
        try {
            annuityService.createAnnuity(request);
        } catch (CreditException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Кредит с аннуитетными платежами создан");
    }


}
