package com.example.bank.repository;

import com.example.bank.entity.Scoring;
import com.example.bank.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoringRepository extends JpaRepository<Scoring, Long> {
    List<Scoring> findByUserAndApprovedIsTrue(User user);

    Scoring findScoringById(Long id);

    Page<Scoring> findAll(Pageable pageable);
}
