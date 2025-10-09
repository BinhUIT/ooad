package com.example.ooad.domain.entity;

import java.sql.Date;

import com.example.ooad.domain.enums.EGender;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="patient")
public class Patient extends Actor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int patientId;
    private String address;
    private Date recordCreateDate;
    private Date firstVisitDate;
    
    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
   
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    
    
    public Date getRecordCreateDate() {
        return recordCreateDate;
    }
    public void setRecordCreateDate(Date recordCreateDate) {
        this.recordCreateDate = recordCreateDate;
    }
    public Date getFirstVisitDate() {
        return firstVisitDate;
    }
    public void setFirstVisitDate(Date firstVisitDate) {
        this.firstVisitDate = firstVisitDate;
    }
   
    public Patient() {
        super();
    }
    public Patient(int patientId, String fullName, Date dateOfBirth, EGender gender, String address, String phone,
            String email, String idCard, Date recordCreateDate, Date firstVisitDate, Account account) {
        super(fullName, dateOfBirth, gender, phone, email, idCard, account);
        this.patientId=patientId;
        this.address=address;
        this.recordCreateDate=recordCreateDate;
        this.firstVisitDate=firstVisitDate;
    }
    

    
}
