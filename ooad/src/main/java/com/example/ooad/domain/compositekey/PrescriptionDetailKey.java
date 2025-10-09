package com.example.ooad.domain.compositekey;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class PrescriptionDetailKey implements Serializable{
    private Integer prescriptionId;
    private Integer medicineId;

    public PrescriptionDetailKey() {
    }
    public Integer getPrescriptionId() {
        return prescriptionId;
    }
    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
    public Integer getMedicineId() {
        return medicineId;
    }
    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }
    public PrescriptionDetailKey(Integer prescriptionId, Integer medicineId) {
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof PrescriptionDetailKey other) {
            return Objects.equals(this.prescriptionId, other.prescriptionId)&&Objects.equals(this.medicineId, other.medicineId);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.prescriptionId, this.medicineId);
    }
}
