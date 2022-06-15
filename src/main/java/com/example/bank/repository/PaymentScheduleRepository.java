package com.example.bank.repository;

import com.example.bank.entity.Credit;
import com.example.bank.entity.PaymentSchedule;
import com.example.bank.entity.PaymentScheduleId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, PaymentScheduleId> {
    Page<PaymentSchedule> findPaymentScheduleByCredit(Credit credit, Pageable pageable);

    PaymentSchedule findTopByCreditAndPaymentMadeIsFalseOrderByPaymentDateAsc(Credit credit);

    Long countByCreditAndPaymentMadeIsFalse(Credit credit);
}
