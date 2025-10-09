package com.example.ooad.domain.compositekey;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MedicinePriceKey implements Serializable{
    private Integer medicineId;
    private Date effectiveDate;
    public Integer getMedicineId() {
        return medicineId;
    }
    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    public MedicinePriceKey() {
    }
    public MedicinePriceKey(Integer medicineId, Date effectiveDate) {
        this.medicineId = medicineId;
        this.effectiveDate = effectiveDate;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof MedicinePriceKey other) {
            return Objects.equals(this.medicineId, other.medicineId)&&Objects.equals(this.effectiveDate, other.effectiveDate);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.effectiveDate, this.medicineId);
    }
    
}
