package com.example.bank.repository;

import com.example.bank.entity.CreditTariff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditTariffRepository extends JpaRepository<CreditTariff, Long> {
    CreditTariff findCreditTariffById(Long id);

    Page<CreditTariff> findAll(Pageable page);
}
