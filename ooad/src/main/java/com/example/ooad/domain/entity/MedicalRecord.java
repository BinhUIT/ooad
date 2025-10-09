package com.example.ooad.domain.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="medical_record")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int recordId;
    @ManyToOne
    @JoinColumn(name="reception_id")
    private Reception reception;
    @ManyToOne
    @JoinColumn(name="doctor_id")
    private Staff doctor;
    private Date examinateDate;
    private String symptoms;
    private String diagnosis;
    private String diseaseType;
    private String orderedServices;
    private String notes;
    public int getRecordId() {
        return recordId;
    }
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
    public Reception getReception() {
        return reception;
    }
    public void setReception(Reception reception) {
        this.reception = reception;
    }
    public Staff getDoctor() {
        return doctor;
    }
    public void setDoctor(Staff doctor) {
        this.doctor = doctor;
    }
    public Date getExaminateDate() {
        return examinateDate;
    }
    public void setExaminateDate(Date examinateDate) {
        this.examinateDate = examinateDate;
    }
    public String getSymptoms() {
        return symptoms;
    }
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }
    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public String getDiseaseType() {
        return diseaseType;
    }
    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }
    public String getOrderedServices() {
        return orderedServices;
    }
    public void setOrderedServices(String orderedServices) {
        this.orderedServices = orderedServices;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public MedicalRecord() {
    }
    public MedicalRecord(int recordId, Reception reception, Staff doctor, Date examinateDate, String symptoms,
            String diagnosis, String diseaseType, String orderedServices, String notes) {
        this.recordId = recordId;
        this.reception = reception;
        this.doctor = doctor;
        this.examinateDate = examinateDate;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.diseaseType = diseaseType;
        this.orderedServices = orderedServices;
        this.notes = notes;
    }
    
}
