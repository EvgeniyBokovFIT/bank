package com.example.bank.service;

import com.example.bank.entity.*;
import com.example.bank.exception.CreditException;
import com.example.bank.repository.*;
import com.example.bank.rest.AnnuityRequestDTO;
import com.example.bank.rest.PaymentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

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

    @Autowired
    PaymentMethodRepository methodRepository;

    @Autowired
    PaymentRepository paymentRepository;

    private Long calculateAnnuity(Double amount, Double rate, Long term) {
        Double monthlyRate = rate / (100 * 12);
        System.out.println(monthlyRate);
        return Math.round(amount * (monthlyRate / (1 - Math.pow(1 + monthlyRate, -term))));
    }

    private Timestamp getTimestampAfterAmountMonth(Long amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(System.currentTimeMillis()));
        cal.add(Calendar.MONTH, Math.toIntExact(amount));
        return new Timestamp(cal.getTime().getTime());
    }

    @Transactional
    public void createAnnuity(AnnuityRequestDTO request) throws CreditException {
        Scoring scoring = scoringRepository.findScoringById(request.getScoringId());
        User user = scoring.getUser();
        List<Scoring> scoringList = user.getScorings();
        for (Scoring scoringFromList : scoringList) {
            if(scoringFromList.getCredit() != null) {
                throw new CreditException("У пользователя уже есть кредит");
            }
        }
        Long monthlyAmount = calculateAnnuity(Double.valueOf(scoring.getAmount()), scoring.getRate(), scoring.getTerm());
        System.out.println(monthlyAmount);
        Credit credit = new Credit();
        credit.setScoring(scoring);
        credit.setStartDate(new Timestamp(System.currentTimeMillis()));
        credit.setEndDate(this.getTimestampAfterAmountMonth(scoring.getTerm()));

        creditRepository.save(credit);

        CreditBalance balance = new CreditBalance();
        balance.setCredit(credit);
        balance.setDebt(0D);
        balance.setCommission(0L);
        balance.setPercents(0D);
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
            schedule.setPaymentMade(false);

            scheduleRepository.save(schedule);
        }

    }

    private void updateBalance(CreditBalance balance, Scoring scoring, Long paymentAmount) {
        Double thisPaymentPercents = (scoring.getAmount() - balance.getDebt() ) * scoring.getRate() / (12 * 100);
        balance.setPercents(balance.getPercents() + thisPaymentPercents);
        Double thisPaymentDebt = paymentAmount - balance.getPercents();
        balance.setDebt(balance.getDebt() + thisPaymentDebt);
        balanceRepository.save(balance);
    }

    private void handleOverpayment(PaymentRequestDTO request, Credit credit, PaymentSchedule closestSchedule) {
        closestSchedule.setAmount(request.getAmount());
        closestSchedule.setPaymentMade(true);
        scheduleRepository.save(closestSchedule);

        Scoring scoring = credit.getScoring();
        CreditBalance balance = balanceRepository.findCreditBalanceByCredit(credit);
        updateBalance(balance, scoring, request.getAmount());

        Double principal = scoring.getAmount() - balance.getDebt();
        Long term = scheduleRepository.countByCreditAndPaymentMadeIsFalse(credit);
        Long newAnnuity = calculateAnnuity(principal, scoring.getRate(), term);

        for(PaymentSchedule schedule : credit.getScheduleList()) {
            if(schedule.getId() != closestSchedule.getId() && !schedule.isPaymentMade()) {
                schedule.setAmount(newAnnuity);
                scheduleRepository.save(schedule);
            }
        }
    }

    private void handleScheduledPayment(PaymentRequestDTO request, Credit credit, PaymentSchedule closestSchedule) {
        closestSchedule.setPaymentMade(true);
        System.out.println(closestSchedule.getPaymentDate());
        scheduleRepository.save(closestSchedule);
        Scoring scoring = credit.getScoring();
        CreditBalance balance = balanceRepository.findCreditBalanceByCredit(credit);
        updateBalance(balance, scoring, request.getAmount());
    }

    public void handlePayment(PaymentRequestDTO request) {
        Credit credit = creditRepository.findCreditById(request.getCreditId());
        PaymentSchedule closestSchedule =
                scheduleRepository.findTopByCreditAndPaymentMadeIsFalseOrderByPaymentDateAsc(credit);

        Payment payment = new Payment();
        payment.setPaymentDate(request.getDate());
        payment.setAmount(request.getAmount());
        payment.setCredit(credit);
        payment.setPaymentMethod(methodRepository.findPaymentMethodById(request.getPaymentMethodId()));
        paymentRepository.save(payment);

        if(request.getAmount() > closestSchedule.getAmount() &&
                request.getDate().before(closestSchedule.getPaymentDate())) {
            handleOverpayment(request, credit, closestSchedule);
            return;
        }

        /*Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(request.getDate().getTime());
        int requestYear = cal.get(Calendar.YEAR);
        int requestMonth = cal.get(Calendar.MONTH);
        cal.setTimeInMillis(closestSchedule.getPaymentDate().getTime());
        int scheduleYear = cal.get(Calendar.YEAR);
        int scheduleMonth = cal.get(Calendar.MONTH);*/

        if(request.getAmount().equals(closestSchedule.getAmount()) &&
                request.getDate().before(closestSchedule.getPaymentDate())) {
            handleScheduledPayment(request, credit, closestSchedule);
            return;
        }
    }
}
