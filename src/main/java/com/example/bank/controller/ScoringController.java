package com.example.bank.controller;

import com.example.bank.exception.ScoringException;
import com.example.bank.rest.ScoringResponseDTO;
import com.example.bank.rest.TariffToUserRequestDTO;
import com.example.bank.service.ScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/admin")
public class ScoringController {

    @Autowired
    ScoringService scoringService;

    @PostMapping("create-scoring")
    ResponseEntity<?> createScoring(@RequestBody TariffToUserRequestDTO request) {
        try {
            scoringService.setTariffToUser(request);
            return ResponseEntity.ok("Тариф присвоен клиенту");
        } catch (ScoringException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get-scorings")
    ResponseEntity<?> getScorings(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(scoringService.getScorings(pageable).map(ScoringResponseDTO::fromScoring));
    }
}
