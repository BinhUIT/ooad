package com.example.ooad.domain.entity;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int invoiceId;
    @ManyToOne
    @JoinColumn(name="patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name="record_id")
    private MedicalRecord record;
    private Date invoiceDate;
    @Column(precision=18, scale=2)
    private BigDecimal examinationFee;
    @Column(precision=18, scale=2)
    private BigDecimal medicineFee;
    @Column(precision=18, scale=2)
    private BigDecimal serviceFee;
    @Column(precision=18, scale=2)
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String paymentStatus;
    @ManyToOne
    @JoinColumn(name="issued_by")
    private Staff issueBy;
    public int getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public MedicalRecord getRecord() {
        return record;
    }
    public void setRecord(MedicalRecord record) {
        this.record = record;
    }
    public Date getInvoiceDate() {
        return invoiceDate;
    }
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    public BigDecimal getExaminationFee() {
        return examinationFee;
    }
    public void setExaminationFee(BigDecimal examinationFee) {
        this.examinationFee = examinationFee;
    }
    public BigDecimal getMedicineFee() {
        return medicineFee;
    }
    public void setMedicineFee(BigDecimal medicineFee) {
        this.medicineFee = medicineFee;
    }
    public BigDecimal getServiceFee() {
        return serviceFee;
    }
    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public Staff getIssueBy() {
        return issueBy;
    }
    public void setIssueBy(Staff issueBy) {
        this.issueBy = issueBy;
    }
    public Invoice() {
    }
    public Invoice(int invoiceId, Patient patient, MedicalRecord record, Date invoiceDate, BigDecimal examinationFee,
            BigDecimal medicineFee, BigDecimal serviceFee, BigDecimal totalAmount, String paymentMethod,
            String paymentStatus, Staff issueBy) {
        this.invoiceId = invoiceId;
        this.patient = patient;
        this.record = record;
        this.invoiceDate = invoiceDate;
        this.examinationFee = examinationFee;
        this.medicineFee = medicineFee;
        this.serviceFee = serviceFee;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.issueBy = issueBy;
    }
    

}
