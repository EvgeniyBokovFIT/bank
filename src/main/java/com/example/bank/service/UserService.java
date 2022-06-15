package com.example.bank.service;

import com.example.bank.entity.BannedUser;
import com.example.bank.entity.Role;
import com.example.bank.entity.Scoring;
import com.example.bank.entity.User;
import com.example.bank.repository.BannedUserRepository;
import com.example.bank.repository.UserRepository;
import com.example.bank.rest.BanUserRequestDTO;
import com.example.bank.rest.UserInfoRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BannedUserRepository bannedUserRepository;

    public void updateUser(UserInfoRequestDTO request) {
        User user = userRepository.findByPassportData(request.getPassportData());
        if(user == null) {
            throw new UsernameNotFoundException("Пользователь с такими паспортными данными не найден");
        }
        if(request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if(request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if(request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        userRepository.save(user);
    }

    private Timestamp calculateBanEndDate(Long days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(System.currentTimeMillis()));
        cal.add(Calendar.DATE, Math.toIntExact(days));
        return new Timestamp(cal.getTime().getTime());
    }

    public void banUser(BanUserRequestDTO request) {
        User userToBan = userRepository.findByMailOrPassportDataOrId(request.getMail(), request.getPassportData(), request.getId());
        if(userToBan == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        userToBan.setRole(new Role(3L, "ROLE_BANNED"));
        BannedUser bannedUser = new BannedUser();
        bannedUser.setUser(userToBan);
        bannedUser.setEndDate(calculateBanEndDate(request.getAmountOfDays()));

        bannedUserRepository.save(bannedUser);
    }

    public void deleteUser(UserInfoRequestDTO request) {
        User user = userRepository.findByPassportData(request.getPassportData());
        if(user == null) {
            throw new UsernameNotFoundException("Пользователь с такими паспортными данными не найден");
        }
        for(Scoring scoring : user.getScorings()) {
            scoring.setCreditTariff(null);
        }
        userRepository.delete(user);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


}
