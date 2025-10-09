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
@Table(name="prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int prescriptionId;
    @ManyToOne
    @JoinColumn(name="record_id")
    private MedicalRecord record;
    private Date prescriptionDate;
    private String notes;
    public Prescription(int prescriptionId, MedicalRecord record, Date prescriptionDate, String notes) {
        this.prescriptionId = prescriptionId;
        this.record = record;
        this.prescriptionDate = prescriptionDate;
        this.notes = notes;
    }

    public Prescription() {
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public MedicalRecord getRecord() {
        return record;
    }

    public void setRecord(MedicalRecord record) {
        this.record = record;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
}
