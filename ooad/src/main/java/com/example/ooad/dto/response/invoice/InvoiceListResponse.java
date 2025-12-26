package com.example.ooad.dto.response.invoice;

import java.math.BigDecimal;
import java.sql.Date;

import com.example.ooad.domain.enums.EPaymentStatus;

public class InvoiceListResponse {
    private int invoiceId;
    private int patientId;
    private String patientName;
    private String patientPhone;
    private Integer recordId;
    private Date invoiceDate;
    private BigDecimal examinationFee;
    private BigDecimal medicineFee;
    private BigDecimal serviceFee;
    private BigDecimal totalAmount;
    private String paymentMethodName;
    private EPaymentStatus paymentStatus;
    private String issuedByName;

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
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

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public EPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(EPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getIssuedByName() {
        return issuedByName;
    }

    public void setIssuedByName(String issuedByName) {
        this.issuedByName = issuedByName;
    }
}
