package com.example.bank.service;

import com.example.bank.entity.*;
import com.example.bank.repository.CreditBalanceRepository;
import com.example.bank.repository.CreditRepository;
import com.example.bank.repository.PaymentScheduleRepository;
import com.example.bank.repository.ScoringRepository;
import com.example.bank.rest.AnnuityRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class AnnuityService {

    @Autowired
    ScoringRepository scoringRepository;

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    CreditBalanceRepository balanceRepository;

    @Autowired
    PaymentScheduleRepository scheduleRepository;

    private Double calculateAnnuity(Long amount, Double rate, Long term) {
        Double monthlyRate = rate * 12 / 100;
        return amount * (monthlyRate / (1 - Math.pow(1 + monthlyRate, -term)));
    }

    private Timestamp getTimestampAfterAmountMonth(Long amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(System.currentTimeMillis()));
        cal.add(Calendar.MONTH, Math.toIntExact(amount));
        return new Timestamp(cal.getTime().getTime());
    }

    @Transactional
    public void createAnnuity(AnnuityRequestDTO request) {
        Scoring scoring = scoringRepository.findScoringById(request.getScoringId());
        Double monthlyAmount = calculateAnnuity(scoring.getAmount(), scoring.getRate(), scoring.getTerm());
        Credit credit = new Credit();
        credit.setScoring(scoring);
        credit.setStartDate(new Timestamp(System.currentTimeMillis()));
        credit.setEndDate(this.getTimestampAfterAmountMonth(scoring.getTerm()));

        creditRepository.save(credit);

        CreditBalance balance = new CreditBalance();
        balance.setCredit(credit);
        balance.setDebt(0L);
        balance.setCommission(0L);
        balance.setPercents(0L);
        balance.setFine(0L);

        balanceRepository.save(balance);

        for(Long i = 0L; i < scoring.getTerm(); i++) {
            PaymentSchedule schedule = new PaymentSchedule();
            PaymentScheduleId scheduleId = new PaymentScheduleId();
            scheduleId.setCredit(credit);
            scheduleId.setPaymentDate(this.getTimestampAfterAmountMonth(i+1));
            schedule.setId(scheduleId);
            schedule.setCredit(scheduleId.getCredit());
            schedule.setPaymentDate(scheduleId.getPaymentDate());
            schedule.setAmount(monthlyAmount);

            scheduleRepository.save(schedule);
        }

    }
}
