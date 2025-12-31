package com.example.ooad.dto.request;

import java.sql.Date;

import com.example.ooad.domain.enums.EGender;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private   Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    private  EGender gender;
    private String phone;
    private String email;
    private String idCard;
    private String address;
    private String password;
}
