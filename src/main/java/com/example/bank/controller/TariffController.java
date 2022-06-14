package com.example.bank.controller;

import com.example.bank.exception.TariffException;
import com.example.bank.rest.TariffRequestDTO;
import com.example.bank.service.CreditTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/tariff")
public class TariffController {

    @Autowired
    CreditTariffService creditTariffService;

    @GetMapping
    public ResponseEntity<?> getTariffs(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(creditTariffService.getTariffs(pageable));
    }

    @PostMapping
    public ResponseEntity<?> createTariff(@RequestBody TariffRequestDTO request) {
        try {
            creditTariffService.createTariff(request);
        } catch (TariffException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Тариф добавлен");
    }

    @PutMapping
    public ResponseEntity<?> updateTariff(@RequestBody TariffRequestDTO request) {
        try {
            creditTariffService.updateTariff(request);
        } catch (TariffException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Тариф обновлен");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTariff(@RequestBody TariffRequestDTO request) {
        try {
            creditTariffService.deleteTariff(request);
        } catch (TariffException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Тариф удален");
    }
}
