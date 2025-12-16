package com.example.ooad.dto.response;

import java.sql.Date;

import com.example.ooad.domain.enums.EGender;

/**
 * DTO Response cho Staff
 */
public class StaffResponse {
    private Integer staffId;
    private String fullName;
    private String position;
    private Date dateOfBirth;
    private EGender gender;
    private String phone;
    private String email;
    private String idCard;
    
    public StaffResponse() {}
    
    public StaffResponse(Integer staffId, String fullName, String position, 
                        Date dateOfBirth, EGender gender, String phone, 
                        String email, String idCard) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.idCard = idCard;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
}
