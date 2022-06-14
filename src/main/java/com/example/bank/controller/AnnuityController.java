package com.example.bank.controller;

import com.example.bank.rest.AnnuityRequestDTO;
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
        annuityService.createAnnuity(request);
        return ResponseEntity.ok("Кредит с аннуитетными платежами создан");
    }

}
