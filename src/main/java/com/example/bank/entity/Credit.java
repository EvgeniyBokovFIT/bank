package com.example.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "credits")
public class Credit {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "scoring_id")
    Scoring scoring;

    private java.sql.Timestamp startDate;

    private java.sql.Timestamp endDate;
}
