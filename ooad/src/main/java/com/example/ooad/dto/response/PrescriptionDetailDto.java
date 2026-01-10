package com.example.ooad.dto.response;

public class PrescriptionDetailDto {
    private MedicineDto medicine;
    private Integer quantity;
    private String dosage;
    private Integer days;
    private String dispenseStatus;

    public PrescriptionDetailDto() {
    }

    public MedicineDto getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineDto medicine) {
        this.medicine = medicine;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getDispenseStatus() {
        return dispenseStatus;
    }

    public void setDispenseStatus(String dispenseStatus) {
        this.dispenseStatus = dispenseStatus;
    }
}
