package com.example.ooad.dto.response;

import java.sql.Date;
import java.util.List;

public class PrescriptionDto {
    private Integer prescriptionId;
    private Date prescriptionDate;
    private String notes;
    private List<PrescriptionDetailDto> prescriptionDetail;

    public PrescriptionDto() {
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<PrescriptionDetailDto> getPrescriptionDetail() {
        return prescriptionDetail;
    }

    public void setPrescriptionDetail(List<PrescriptionDetailDto> prescriptionDetail) {
        this.prescriptionDetail = prescriptionDetail;
    }
}
