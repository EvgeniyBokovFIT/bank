package com.example.bank.controller;

import com.example.bank.exception.MailException;
import com.example.bank.exception.PasswordException;
import com.example.bank.rest.BanUserRequestDTO;
import com.example.bank.rest.UserInfoRequestDTO;
import com.example.bank.rest.UserCreditRequestDTO;
import com.example.bank.rest.UsersResponseDTO;
import com.example.bank.service.AuthService;
import com.example.bank.service.UserCreditService;
import com.example.bank.service.UserDetailsServiceImpl;
import com.example.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/admin")
public class UserController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserCreditService userCreditService;

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable).map(UsersResponseDTO::fromUser));
    }

    @PostMapping("/ban")
    public ResponseEntity<?> banUser(@RequestBody BanUserRequestDTO request) {
        try {
            userService.banUser(request);
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Пользователь заблокирован на " + request.getAmountOfDays() + " дней");
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserInfoRequestDTO request) {
        try {
            authService.register(request);
        } catch (MailException | PasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Пользователь добавлен");
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UserInfoRequestDTO request) {
        try {
            userService.updateUser(request);
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("Данные пользователя изменены");
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestBody UserInfoRequestDTO request) {
        try {
            userService.deleteUser(request);
        }
        catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("Пользователь удален");
    }

    @GetMapping("/users-credits")
    public ResponseEntity<?> getUsersCredits(@RequestBody UserCreditRequestDTO request,
                                             @PageableDefault(sort = {"scoring.amount"}, direction = Sort.Direction.DESC) Pageable pageable) {
        System.out.println(pageable.getPageSize());
        return ResponseEntity.ok(userCreditService.findAllWithFilters(request, pageable));
    }

}
