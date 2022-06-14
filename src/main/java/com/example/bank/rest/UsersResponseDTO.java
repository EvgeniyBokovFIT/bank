package com.example.bank.rest;

import com.example.bank.entity.User;
import lombok.Data;

@Data
public class UsersResponseDTO {
    private Long id;

    private String mail;

    private String firstName;

    private String lastName;

    private String passportData;

    private String role;

    public static UsersResponseDTO fromUser(User user) {
        UsersResponseDTO response = new UsersResponseDTO();
        response.setId(user.getId());
        response.setMail(user.getMail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPassportData(user.getPassportData());
        response.setRole(user.getRole().getName());
        return response;
    }
}
