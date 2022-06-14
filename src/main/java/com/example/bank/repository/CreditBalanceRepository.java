package com.example.bank.repository;

import com.example.bank.entity.CreditBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditBalanceRepository extends JpaRepository<CreditBalance, Long> {
}
