package com.example.ooad.domain.entity;

import com.example.ooad.domain.enums.EMedicineStorageCondition;
import com.example.ooad.domain.enums.EMedicineUnit;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int medicineId;
    private String medicineName;
    @Enumerated(EnumType.STRING)
    private EMedicineUnit unit=EMedicineUnit.TABLET;
    private String concentration;
    private String form;
    private String manufacturer;
    private String usageInstructions;
    private String image;
    @Enumerated(EnumType.STRING)
    private EMedicineStorageCondition storageCondition=EMedicineStorageCondition.NORMAL;
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
    public Medicine() {
    }
    public Medicine(int medicineId, String medicineName, EMedicineUnit unit, String concentration, String form,
            String manufacturer, String usageInstructions, String image, EMedicineStorageCondition storageCondition) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.unit = unit;
        this.concentration = concentration;
        this.form = form;
        this.manufacturer = manufacturer;
        this.usageInstructions = usageInstructions;
        this.image = image;
        this.storageCondition = storageCondition;
    }
    
}
