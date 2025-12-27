package com.example.ooad.dto.request;

import java.sql.Date;

public class UpdateMedicalRecordRequest {
    private Date examinateDate;

    private String diagnosis;

    private String symptoms;

    private Integer diseaseTypeId;

    private String orderedServices;

    private String notes;

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
}
