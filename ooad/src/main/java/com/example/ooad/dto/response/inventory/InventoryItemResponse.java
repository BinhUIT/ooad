package com.example.ooad.dto.response.inventory;

import java.sql.Date;

public class InventoryItemResponse {
    private int medicineId;
    private String medicineName;
    private String unit;
    private int totalQuantity;
    private Date nearestExpiryDate;
    private String manufacturer;
    private String concentration;
    private String storageCondition;

    public InventoryItemResponse() {
    }

    public InventoryItemResponse(int medicineId, String medicineName, String unit, int totalQuantity,
            Date nearestExpiryDate, String manufacturer, String concentration, String storageCondition) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.unit = unit;
        this.totalQuantity = totalQuantity;
        this.nearestExpiryDate = nearestExpiryDate;
        this.manufacturer = manufacturer;
        this.concentration = concentration;
        this.storageCondition = storageCondition;
    }

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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Date getNearestExpiryDate() {
        return nearestExpiryDate;
    }

    public void setNearestExpiryDate(Date nearestExpiryDate) {
        this.nearestExpiryDate = nearestExpiryDate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getStorageCondition() {
        return storageCondition;
    }

    public void setStorageCondition(String storageCondition) {
        this.storageCondition = storageCondition;
    }
}
