package com.example.ooad.dto.request;

import java.sql.Date;

import jakarta.validation.constraints.NotNull;

public class CreateMedicalRecordRequest {
    @NotNull(message = "Reception ID is required")
    private Integer receptionId;

    private Date examinateDate;

    private String diagnosis;

    private String symptoms;

    private Integer diseaseTypeId;

    private String orderedServices;

    private String notes;

    private Boolean createInvoice = true;

    public Integer getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(Integer receptionId) {
        this.receptionId = receptionId;
    }

    public Date getExaminateDate() {
        return examinateDate;
    }

    public void setExaminateDate(Date examinateDate) {
        this.examinateDate = examinateDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public Integer getDiseaseTypeId() {
        return diseaseTypeId;
    }

    public void setDiseaseTypeId(Integer diseaseTypeId) {
        this.diseaseTypeId = diseaseTypeId;
    }

    public String getOrderedServices() {
        return orderedServices;
    }

    public void setOrderedServices(String orderedServices) {
        this.orderedServices = orderedServices;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getCreateInvoice() {
        return createInvoice;
    }

    public void setCreateInvoice(Boolean createInvoice) {
        this.createInvoice = createInvoice;
    }
}
