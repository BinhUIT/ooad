package com.example.ooad.dto.request;

import java.sql.Date;

import com.example.ooad.domain.enums.EGender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class PatientRequest {
    @NotNull(message="Address is required")
    private String address;
    @NotNull(message="First visit date is required")
    private Date firstVisitDate;
    @NotNull(message="Full name is required")
    private String fullName;
    @NotNull(message="Date of birth is required")
    private Date dateOfBirth;
    @NotNull(message = "Gender is required")
    private EGender gender;
    @NotNull(message="Email is invalid")
    @Email(message = "Invalid email")
    private String email;
    
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getIdCard() {
        return idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    @NotNull(message="Phone number is invalid")
    private String phone;
    @NotNull(message="Id card is required")
    private String idCard;
    public PatientRequest() {
    }
    public PatientRequest(@NotNull(message = "Address is required") String address,
            @NotNull(message = "First visit date is required") Date firstVisitDate,
            @NotNull(message = "Full name is required") String fullName,
            @NotNull(message = "Date of birth is required") Date dateOfBirth,
            @NotNull(message = "Gender is required") EGender gender,
            @NotNull(message = "Email is invalid") String email,
            @NotNull(message = "Phone number is invalid") String phone,
            @NotNull(message = "Id card is required") String idCard) {
        this.address = address;
        this.firstVisitDate = firstVisitDate;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.idCard = idCard;
    }
    

}
