package com.example.bank.rest;

import com.example.bank.entity.PaymentSchedule;
import lombok.Data;

@Data
public class UserProfileScheduleResponseDTO {
    private java.sql.Timestamp paymentDate;

    private Double amount;

    public static UserProfileScheduleResponseDTO fromSchedule(PaymentSchedule schedule) {
        UserProfileScheduleResponseDTO response = new UserProfileScheduleResponseDTO();
        response.setPaymentDate(schedule.getPaymentDate());
        response.setAmount(schedule.getAmount());
        return response;
    }
}
