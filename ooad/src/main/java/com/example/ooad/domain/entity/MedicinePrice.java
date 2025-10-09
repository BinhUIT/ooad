package com.example.ooad.domain.entity;

import java.math.BigDecimal;

import com.example.ooad.domain.compositekey.MedicinePriceKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name="medicine_price")
public class MedicinePrice {
    @EmbeddedId
    private MedicinePriceKey medicinePriceId;
    @ManyToOne
    @MapsId("medicineId")
    @JoinColumn(name="medicine_id")
    private Medicine medicine;
    
    @Column(precision=18, scale=2)
    private BigDecimal unitPrice;
    public MedicinePriceKey getMedicinePriceId() {
        return medicinePriceId;
    }
    public void setMedicinePriceId(MedicinePriceKey medicinePriceId) {
        this.medicinePriceId = medicinePriceId;
    }
    public Medicine getMedicine() {
        return medicine;
    }
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    public MedicinePrice() {
    }
    public MedicinePrice(MedicinePriceKey medicinePriceId, BigDecimal unitPrice, Medicine medicine) {
        this.medicinePriceId = medicinePriceId;
        this.unitPrice = unitPrice;
        this.medicine= medicine;
    }
    
}