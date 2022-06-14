package com.example.bank.service;

import com.example.bank.entity.CreditTariff;
import com.example.bank.entity.Scoring;
import com.example.bank.entity.User;
import com.example.bank.exception.ScoringException;
import com.example.bank.repository.CreditTariffRepository;
import com.example.bank.repository.ScoringRepository;
import com.example.bank.repository.UserRepository;
import com.example.bank.rest.TariffToUserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ScoringService {

    @Autowired
    ScoringRepository scoringRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CreditTariffRepository tariffRepository;

    public void setTariffToUser(TariffToUserRequestDTO request) throws ScoringException {
        User user = userRepository.findUserById(request.getUserId());
        CreditTariff creditTariff = tariffRepository.findCreditTariffById(request.getTariffId());
        if(request.getAmount() > creditTariff.getMaxAmount() || request.getAmount() < creditTariff.getMinAmount()
            || request.getRate() > creditTariff.getMaxRate() || request.getRate() < creditTariff.getMinRate()
            || request.getTerm() > creditTariff.getMaxTerm() || request.getTerm() < creditTariff.getMinTerm()) {
            throw new ScoringException("Данные кредита не соответствуют выбранному тарифу");
        }
        Scoring scoring = new Scoring();
        scoring.setUser(user);
        scoring.setCreditTariff(creditTariff);
        scoring.setAmount(request.getAmount());
        scoring.setRate(request.getRate());
        scoring.setTerm(request.getTerm());
        scoring.setApproved(request.isApproved());
        scoring.setApplicationDate(new Timestamp(System.currentTimeMillis()));

        scoringRepository.save(scoring);
    }

    public Page<Scoring> getScorings(Pageable pageable) {
        return scoringRepository.findAll(pageable);
    }
}
