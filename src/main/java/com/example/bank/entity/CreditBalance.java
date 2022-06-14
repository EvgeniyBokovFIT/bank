package com.example.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "credits_balance")
public class CreditBalance {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "credit_id")
    Credit credit;

    private Long debt;

    private Long percents;

    private Long commission;

    private Long fine;
}
