package com.example.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "scoring")
public class Scoring {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private CreditTariff creditTariff;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long term;

    private Long amount;

    private Double rate;

    private java.sql.Timestamp applicationDate;

    private boolean approved;

}
