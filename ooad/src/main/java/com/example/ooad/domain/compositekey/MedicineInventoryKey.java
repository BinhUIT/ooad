package com.example.ooad.domain.compositekey;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MedicineInventoryKey implements Serializable {
    private Integer importId;
    private Integer medicineId;
    public Integer getImportId() {
        return importId;
    }
    public void setImportId(Integer importId) {
        this.importId = importId;
    }
    public Integer getMedicineId() {
        return medicineId;
    }
    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }
    public MedicineInventoryKey() {
    }
    public MedicineInventoryKey(Integer importId, Integer medicineId) {
        this.importId = importId;
        this.medicineId = medicineId;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof MedicineInventoryKey other) {
            return Objects.equals(this.importId, other.importId)&&Objects.equals(this.medicineId, other.medicineId);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.importId, this.medicineId);
    }
}
