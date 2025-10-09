package com.example.ooad.domain.entity;

import com.example.ooad.domain.compositekey.PrescriptionDetailKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name="prescription_detail")
public class PrescriptionDetail {
    @EmbeddedId
    private PrescriptionDetailKey prescriptionDetailId;
    @ManyToOne
    @MapsId("prescriptionId")
    @JoinColumn(name="prescription_id")
    private Prescription prescription;
    @ManyToOne
    @MapsId("medicineId")
    @JoinColumn(name="medicine_id")
    private Medicine medicine;
    private int quantity;
    @Column(name="dosage", columnDefinition="VARCHAR(100)")
    private String dosage;
    private int days;
    private String dispenseStatus;
    public PrescriptionDetailKey getPrescriptionDetailId() {
        return prescriptionDetailId;
    }
    public void setPrescriptionDetailId(PrescriptionDetailKey prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
    }
    public Prescription getPrescription() {
        return prescription;
    }
    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }
    public Medicine getMedicine() {
        return medicine;
    }
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getDosage() {
        return dosage;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    public int getDays() {
        return days;
    }
    public void setDays(int days) {
        this.days = days;
    }
    public String getDispenseStatus() {
        return dispenseStatus;
    }
    public void setDispenseStatus(String dispenseStatus) {
        this.dispenseStatus = dispenseStatus;
    }
    public PrescriptionDetail() {
    }
    public PrescriptionDetail(PrescriptionDetailKey prescriptionDetailId, Prescription prescription, Medicine medicine,
            int quantity, String dosage, int days, String dispenseStatus) {
        this.prescriptionDetailId = prescriptionDetailId;
        this.prescription = prescription;
        this.medicine = medicine;
        this.quantity = quantity;
        this.dosage = dosage;
        this.days = days;
        this.dispenseStatus = dispenseStatus;
    }
    
}
