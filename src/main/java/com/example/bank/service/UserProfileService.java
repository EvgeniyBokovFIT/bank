package com.example.bank.service;

import com.example.bank.entity.*;
import com.example.bank.exception.CreditException;
import com.example.bank.repository.*;
import com.example.bank.rest.UserProfileCreditResponse;
import com.example.bank.rest.UserProfileScheduleResponseDTO;
import com.example.bank.rest.UsersResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserProfileService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ScoringRepository scoringRepository;

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    CreditTariffRepository creditTariffRepository;

    @Autowired
    PaymentScheduleRepository scheduleRepository;

    private User getUserByAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();

        return userRepository.findByMail(mail);
    }

    private Credit findCreditByAuthentication() {
        User user = this.getUserByAuthentication();

        List<Scoring> scorings = scoringRepository.findByUserAndApprovedIsTrue(user);

        List<Long> scoringsId = new ArrayList<>();
        for(Scoring scoring : scorings) {
            scoringsId.add(scoring.getId());
        }

        Credit credit = creditRepository.findByEndDateAfterAndIdIsIn(new Timestamp(System.currentTimeMillis()), scoringsId);

        return credit;
    }

    public UserProfileCreditResponse getMyCredit() {

        Credit credit = this.findCreditByAuthentication();

        Scoring scoring = scoringRepository.findScoringById(credit.getId());

        UserProfileCreditResponse response = new UserProfileCreditResponse();
        response.setAmount(scoring.getAmount());
        response.setRate(scoring.getRate());
        response.setStartDate(credit.getStartDate());
        response.setEndDate(credit.getEndDate());

        return response;
    }

    public Page<PaymentSchedule> getMySchedule(Pageable pageable) throws CreditException {
        Credit credit = this.findCreditByAuthentication();
        if(credit == null) {
            throw new CreditException("Кредит не найден");
        }
        return scheduleRepository.findPaymentScheduleByCredit(credit, pageable);
    }

    public UsersResponseDTO getMyPersonalInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();

        User user = userRepository.findByMail(mail);
        return UsersResponseDTO.fromUser(user);
    }

    public Page<UserProfileCreditResponse> getMyCreditHistory(Pageable pageable) {
        User user = this.getUserByAuthentication();
        return creditRepository.findCreditHistory(user.getId(), pageable);
    }

}
