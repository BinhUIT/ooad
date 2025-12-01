package com.example.ooad.dto.response;

import java.sql.Date;

import com.example.ooad.domain.enums.EGender;
import com.example.ooad.domain.enums.ERole;

/**
 * DTO trả về thông tin profile của user.
 * Không chứa các thông tin nhạy cảm như password.
 */
public class ProfileResponse {
    
    private String username;
    private ERole role;
    private String fullName;
    private Date dateOfBirth;
    private EGender gender;
    private String phone;
    private String email;
    private String idCard;
    
    // Thông tin riêng cho Patient
    private String address;
    private Date firstVisitDate;
    
    // Thông tin riêng cho Staff
    private String position;

    public ProfileResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getFirstVisitDate() {
        return firstVisitDate;
    }

    public void setFirstVisitDate(Date firstVisitDate) {
        this.firstVisitDate = firstVisitDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
