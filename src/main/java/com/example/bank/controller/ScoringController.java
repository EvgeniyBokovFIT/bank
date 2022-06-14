package com.example.bank.controller;

import com.example.bank.exception.ScoringException;
import com.example.bank.rest.ScoringResponseDTO;
import com.example.bank.rest.ScoringRequestDTO;
import com.example.bank.service.ScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/admin/scoring")
public class ScoringController {

    @Autowired
    ScoringService scoringService;

    @PostMapping()
    ResponseEntity<?> createScoring(@RequestBody ScoringRequestDTO request) {
        try {
            scoringService.setTariffToUser(request);
            return ResponseEntity.ok("Тариф присвоен клиенту");
        } catch (ScoringException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    ResponseEntity<?> getScorings(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(scoringService.getScorings(pageable).map(ScoringResponseDTO::fromScoring));
    }

    @PutMapping()
    ResponseEntity<?> updateScoring(@RequestBody ScoringRequestDTO request) {
        try {
            scoringService.updateScoring(request);
        } catch (ScoringException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Скоринг обновлен");
    }

    @DeleteMapping
    ResponseEntity<?> deleteScoring(@RequestBody ScoringRequestDTO request) {
        try {
            scoringService.deleteScoring(request);
        } catch (ScoringException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Скоринг удален");
    }

}
