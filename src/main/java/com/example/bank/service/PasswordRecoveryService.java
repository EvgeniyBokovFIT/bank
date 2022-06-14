package com.example.bank.service;

import com.example.bank.entity.User;
import com.example.bank.exception.PasswordException;
import com.example.bank.exception.RecoveryCodeException;
import com.example.bank.repository.UserRepository;
import com.example.bank.rest.PasswordRecoveryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordRecoveryService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void sendMailToPasswordRecovery(String mail) {
        User user = userRepository.findByMail(mail);
        if(user == null) {
            throw new UsernameNotFoundException("Пользователь не найден " + mail);
        }
        String code = UUID.randomUUID().toString();
        user.setRecoveryCode(passwordEncoder.encode(code));
        userRepository.save(user);
        String message = String.format(
                "Здравствуйте, %s! \n" +
                        "Код подтверждения: %s",
                user.getFirstName(),
                code
        );

        mailSenderService.send(user.getMail(), "Восстановление пароля", message);
    }

    public void recoverPassword(PasswordRecoveryDTO request) throws PasswordException, RecoveryCodeException {
        User user = userRepository.findByMail(request.getMail());
        if(user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        if(!passwordEncoder.matches(request.getRecoveryCode(), user.getRecoveryCode())) {
            throw new RecoveryCodeException("Неверный код подтверждения");
        }
        if(!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new PasswordException("Пароли не совпадают");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
