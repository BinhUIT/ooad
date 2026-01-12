package com.example.ooad.dto.response;

import java.sql.Date;
import java.util.List;

public class MedicalRecordDetailResponse {
    private Integer recordId;
    private Integer receptionId;
    private String receptionStatus;
    private String invoicePaymentStatus; // PAID or UNPAID
    private PatientDto patient;
    private Integer doctorId;
    private String doctorName;
    private Date examinateDate;
    private String symptoms;
    private String diagnosis;
    private DiseaseTypeDto diseaseType;
    private String notes;
    private List<OrderedServiceDto> orderedServices; // Array of services (matches FE interface)
    private PrescriptionDto prescription;

    public MedicalRecordDetailResponse() {
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(Integer receptionId) {
        this.receptionId = receptionId;
    }

    public String getReceptionStatus() {
        return receptionStatus;
    }

    public void setReceptionStatus(String receptionStatus) {
        this.receptionStatus = receptionStatus;
    }

    public String getInvoicePaymentStatus() {
        return invoicePaymentStatus;
    }

    public void setInvoicePaymentStatus(String invoicePaymentStatus) {
        this.invoicePaymentStatus = invoicePaymentStatus;
    }

    public PatientDto getPatient() {
        return patient;
    }

    public void setPatient(PatientDto patient) {
        this.patient = patient;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Date getExaminateDate() {
        return examinateDate;
    }

    public void setExaminateDate(Date examinateDate) {
        this.examinateDate = examinateDate;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public DiseaseTypeDto getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(DiseaseTypeDto diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<OrderedServiceDto> getOrderedServices() {
        return orderedServices;
    }

    public void setOrderedServices(List<OrderedServiceDto> orderedServices) {
        this.orderedServices = orderedServices;
    }

    public PrescriptionDto getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionDto prescription) {
        this.prescription = prescription;
    }
}
