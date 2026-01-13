package com.example.ooad.dto.response.inventory;

public class MedicineSelectionResponse {
    private int medicineId;
    private String medicineName;
    private String unit;
    private String concentration;
    private String manufacturer;

    public MedicineSelectionResponse() {
    }

    public MedicineSelectionResponse(int medicineId, String medicineName, String unit, String concentration, String manufacturer) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.unit = unit;
        this.concentration = concentration;
        this.manufacturer = manufacturer;
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
