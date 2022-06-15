package com.example.bank.controller;

import com.example.bank.entity.User;
import com.example.bank.exception.UserIsBannedException;
import com.example.bank.repository.UserRepository;
import com.example.bank.rest.AuthenticationRequestDTO;
import com.example.bank.security.JwtTokenProvider;
import com.example.bank.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin( origins = "*", maxAge = 3500)
@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
        try {
            Map<Object, Object> response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | UserIsBannedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Неверная почта или пароль", HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}