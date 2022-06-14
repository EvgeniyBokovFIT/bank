package com.example.bank.service;

import com.example.bank.entity.User;
import com.example.bank.repository.UserRepository;
import com.example.bank.rest.UserCreditRequestDTO;
import com.example.bank.rest.UserCreditResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserCreditService {

    @Autowired
    UserRepository userRepository;

    public Page<UserCreditResponseDTO> findAllWithFilters(UserCreditRequestDTO request, Pageable pageable) {
        return userRepository.findUserCredits(request.getCreditMinStartDate(), request.getCreditMaxStartDate(),
                request.getCreditMinEndDate(), request.getCreditMaxEndDate(), request.getCreditMinAmount(), request.getCreditMaxAmount(),
                request.getFirstName(), request.getLastName(), pageable);
    }
}
