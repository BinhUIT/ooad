package com.example.ooad.dto.response.inventory;

import com.example.ooad.domain.enums.EMedicineStorageCondition;
import com.example.ooad.domain.enums.EMedicineUnit;

public class MedicineResponse {
    private int medicineId;
    private String medicineName;
    private EMedicineUnit unit;
    private String concentration;
    private String form;
    private String manufacturer;
    private String usageInstructions;
    private String image;
    private EMedicineStorageCondition storageCondition;
    private Integer totalQuantity;

    public MedicineResponse() {
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

    public EMedicineUnit getUnit() {
        return unit;
    }

    public void setUnit(EMedicineUnit unit) {
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

    public EMedicineStorageCondition getStorageCondition() {
        return storageCondition;
    }

    public void setStorageCondition(EMedicineStorageCondition storageCondition) {
        this.storageCondition = storageCondition;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
