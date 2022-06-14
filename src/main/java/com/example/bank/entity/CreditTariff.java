package com.example.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "credit_tariffs")
public class CreditTariff {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long minTerm;

    private Long maxTerm;

    private Long minAmount;

    private Long maxAmount;

    private Double minRate;

    private Double maxRate;

    @OneToMany(mappedBy = "creditTariff")
    List<Scoring> scorings;
}
