package com.example.bank.repository;

import com.example.bank.entity.User;
import com.example.bank.rest.UserCreditResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByMail(String mail);

    User findByMailOrPassportDataOrId(@Nullable String mail, @Nullable String passportData, @Nullable Long Id);

    Page<User> findAll(Pageable pageable);

    User findByPassportData(String passportData);

    User findUserById(Long id);


    @Query("select new com.example.bank.rest.UserCreditResponseDTO(u.firstName, u.lastName, s.term, s.rate, s.amount, c.startDate, c.endDate)" +
            " from Credit c JOIN c.scoring s JOIN s.user u " +
            "where ((cast(:minStartDate as date) is null) or c.startDate >= :minStartDate)" +
            "and ((cast(:maxStartDate as date) is null) or c.startDate <= :maxStartDate)" +
            "and ((cast(:minEndDate as date) is null) or c.endDate >= :minEndDate)" +
            "and ((cast(:minEndDate as date) is null) or c.startDate <= :maxEndDate)" +
            "and (:minAmount is null or s.amount >= :minAmount)" +
            "and (:maxAmount is null or s.amount <= :maxAmount)" +
            "and (:firstName is null or u.firstName = :firstName)" +
            "and (:lastName is null or u.lastName = :lastName)"
    )
    Page<UserCreditResponseDTO> findUserCredits(@Param("minStartDate") Timestamp minStartDate, @Param("maxStartDate") Timestamp maxStartDate,
                                                @Param("minEndDate") Timestamp minEndDate, @Param("maxEndDate") Timestamp maxEndDate,
                                                @Param("minAmount") Long minAmount, @Param("maxAmount") Long maxAmount,
                                                @Param("firstName") String firstName, @Param("lastName") String lastName, Pageable pageable);
}
