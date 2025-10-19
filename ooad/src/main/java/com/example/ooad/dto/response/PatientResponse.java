package com.example.ooad.dto.response;

import java.sql.Date;

import com.example.ooad.domain.enums.EGender;

public class PatientResponse {
    private int patientId;
    private String address;
    private Date firstVisitDate;
    private String fullName;
    private Date dateOfBirth;
    private EGender gender;
    private String email;
    private String phone;
    private String idCard;
    public PatientResponse() {
    }
    public PatientResponse(int patientId,String address, Date firstVisitDate, String fullName, Date dateOfBirth, EGender gender,
            String email, String phone, String idCard) {
        this.address = address;
        this.firstVisitDate = firstVisitDate;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.idCard = idCard;
        this.patientId= patientId;
    }
    public String getAddress() {
        return address;
    }
    public int getPatientId() {
        return this.patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    
}
