package com.example.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "payment_schedule")
public class PaymentSchedule {
    @EmbeddedId
    private PaymentScheduleId id;

    @ManyToOne
    @JoinColumn(name = "credit_id", insertable = false, updatable = false)
    private Credit credit;

    @Column(name = "date", insertable = false, updatable = false)
    private java.sql.Timestamp paymentDate;

    private Long amount;

}
