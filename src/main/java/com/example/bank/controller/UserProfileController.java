package com.example.bank.controller;

import com.example.bank.exception.CreditException;
import com.example.bank.rest.UserProfileScheduleResponseDTO;
import com.example.bank.rest.UsersResponseDTO;
import com.example.bank.service.UserProfileService;
import com.example.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @GetMapping("/personal-info")
    public ResponseEntity<?> getMyPersonalInfo() {
        return ResponseEntity.ok(userProfileService.getMyPersonalInfo());
    }

    @GetMapping("/my-credit")
    public ResponseEntity<?> getMyCredit() {
        try {
            return ResponseEntity.ok(userProfileService.getMyCredit());
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Ошибка аутентификации");
        }

    }

    @GetMapping("/schedule")
    public ResponseEntity<?> getMySchedule(@PageableDefault(sort = {"paymentDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            return ResponseEntity.ok(userProfileService.getMySchedule(pageable).map(UserProfileScheduleResponseDTO::fromSchedule));
        } catch (CreditException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Ошибка аутентификации");
        }

    }

    @GetMapping("/credit-history")
    public ResponseEntity<?> getMyCreditHistory(@PageableDefault(sort = {"endDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userProfileService.getMyCreditHistory(pageable));
    }
}
