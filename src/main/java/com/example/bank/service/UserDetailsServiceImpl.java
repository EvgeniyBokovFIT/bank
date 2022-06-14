package com.example.bank.service;

import com.example.bank.entity.BannedUser;
import com.example.bank.entity.User;
import com.example.bank.repository.BannedUserRepository;
import com.example.bank.repository.RoleRepository;
import com.example.bank.repository.UserRepository;
import com.example.bank.rest.BanUserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BannedUserRepository bannedUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUserByMail(email);
        return new org.springframework.security.core.userdetails.User(
                user.getMail(), user.getPassword(),
                user.getAuthorities());
    }

    public User findUserByMail(String mail) throws UsernameNotFoundException {
        User user = userRepository.findByMail(mail);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }


}
