package com.example.ooad.dto.response.invoice;

import java.math.BigDecimal;
import java.sql.Date;

public class AvailableMedicineResponse {
    private int medicineId;
    private String medicineName;
    private String unit;
    private String concentration;
    private String form;
    private String manufacturer;
    private int availableQuantity;
    private BigDecimal unitPrice;
    private Date nearestExpiryDate;

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Date getNearestExpiryDate() {
        return nearestExpiryDate;
    }

    public void setNearestExpiryDate(Date nearestExpiryDate) {
        this.nearestExpiryDate = nearestExpiryDate;
    }
}
