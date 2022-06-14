package com.example.bank.repository;

import com.example.bank.entity.BannedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannedUserRepository extends JpaRepository<BannedUser, Long> {
}
