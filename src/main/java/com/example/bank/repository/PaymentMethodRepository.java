package com.example.bank.repository;

import com.example.bank.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    PaymentMethod findPaymentMethodById(Long id);
}
