package com.example.bank.service;

import com.example.bank.entity.Role;
import com.example.bank.entity.User;
import com.example.bank.exception.MailException;
import com.example.bank.exception.PasswordException;
import com.example.bank.repository.UserRepository;
import com.example.bank.rest.AuthenticationRequestDTO;
import com.example.bank.rest.UserInfoRequestDTO;
import com.example.bank.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AuthService {
    private final Integer minPasswordLength = 6;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public void register(UserInfoRequestDTO request) throws MailException, PasswordException {

        if(!Pattern.compile(regexPattern).matcher(request.getMail()).matches()) {
            throw new MailException("Некорректный формат почты");
        }
        if (userRepository.findByMail(request.getMail()) != null) {
            throw new MailException("Пользователь с такой почтой уже существует");
        }
        User user = new User();
        user.setMail(request.getMail());
        if(!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new PasswordException("Пароли не совпадают");
        }
        if(request.getPassword().length() < minPasswordLength) {
            throw new PasswordException("Минимальная длина пароля - " + minPasswordLength + " символов");
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassportData(request.getPassportData());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(new Role(1L, "ROLE_USER"));
        userRepository.save(user);
    }

    public Map<Object, Object> authenticate(AuthenticationRequestDTO request) throws AuthenticationException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword()));
            User user = userRepository.findByMail(request.getMail());
            if (user == null) {
                throw new UsernameNotFoundException("Пользователь не найден");
            }
            String token = jwtTokenProvider.createToken(request.getMail(), user.getRole().getName());
            Map<Object, Object> response = new HashMap<>();
            response.put("mail", request.getMail());
            response.put("token", token);
            return response;
        } catch (AuthenticationException e) {
            throw e;
        }
    }
}
