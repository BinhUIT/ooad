package com.example.ooad.dto.response.inventory;

import java.sql.Date;
import java.util.List;

public class InventoryDetailResponse {
    private int medicineId;
    private String medicineName;
    private String unit;
    private String concentration;
    private String form;
    private String manufacturer;
    private String usageInstructions;
    private String image;
    private String storageCondition;
    private int totalQuantity;
    private Date nearestExpiryDate;
    private List<InventoryBatchResponse> batches; // List of inventory batches from different imports

    public InventoryDetailResponse() {
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

    public String getUsageInstructions() {
        return usageInstructions;
    }

    public void setUsageInstructions(String usageInstructions) {
        this.usageInstructions = usageInstructions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStorageCondition() {
        return storageCondition;
    }

    public void setStorageCondition(String storageCondition) {
        this.storageCondition = storageCondition;
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

    public List<InventoryBatchResponse> getBatches() {
        return batches;
    }

    public void setBatches(List<InventoryBatchResponse> batches) {
        this.batches = batches;
    }
}
