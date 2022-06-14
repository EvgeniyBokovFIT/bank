package com.example.bank.repository;

import com.example.bank.entity.Credit;
import com.example.bank.entity.User;
import com.example.bank.rest.UserProfileCreditResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    Credit findCreditById(Long id);

    Credit findByEndDateAfterAndIdIsIn(Timestamp nowDate, List<Long> id);

    @Query("select new com.example.bank.rest.UserProfileCreditResponse(s.amount, s.rate, c.startDate, c.endDate) " +
            "from Credit c JOIN c.scoring s " +
            "where s.user.id = :user_id")
    Page<UserProfileCreditResponse> findCreditHistory(@Param("user_id")Long userId, Pageable pageable);
}
