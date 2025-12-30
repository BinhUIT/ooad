package com.example.ooad.dto.request.inventory;

import com.example.ooad.domain.enums.EMedicineStorageCondition;
import com.example.ooad.domain.enums.EMedicineUnit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MedicineRequest {

    @NotBlank(message = "Medicine name is required")
    @Size(max = 100, message = "Medicine name must not exceed 100 characters")
    private String medicineName;

    @NotNull(message = "Unit is required")
    private EMedicineUnit unit;

    @Size(max = 50, message = "Concentration must not exceed 50 characters")
    private String concentration;

    @Size(max = 50, message = "Form must not exceed 50 characters")
    private String form;

    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    private String manufacturer;

    private String usageInstructions;

    private String image;

    private EMedicineStorageCondition storageCondition;

    public MedicineRequest() {
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
}
