package com.example.ooad.dto.response.invoice;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.example.ooad.domain.enums.EPaymentStatus;

public class InvoiceDetailResponse {
    private int invoiceId;
    private PatientInfo patient;
    private RecordInfo record;
    private Date invoiceDate;
    private BigDecimal examinationFee;
    private BigDecimal medicineFee;
    private BigDecimal serviceFee;
    private BigDecimal totalAmount;
    private PaymentMethodInfo paymentMethod;
    private EPaymentStatus paymentStatus;
    private StaffInfo issuedBy;
    private List<InvoiceServiceDetailResponse> serviceDetails;
    private List<InvoiceMedicineDetailResponse> medicineDetails;

    // Nested classes
    public static class PatientInfo {
        private int patientId;
        private String fullName;
        private String phone;
        private String address;
        private Date dateOfBirth;

        public int getPatientId() {
            return patientId;
        }

        public void setPatientId(int patientId) {
            this.patientId = patientId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(Date dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
    }

    public static class RecordInfo {
        private int recordId;
        private Date examinateDate;
        private String diagnosis;
        private String symptoms;
        private String doctorName;

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
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

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }
    }

    public static class PaymentMethodInfo {
        private int paymentMethodId;
        private String methodCode;
        private String methodName;

        public int getPaymentMethodId() {
            return paymentMethodId;
        }

        public void setPaymentMethodId(int paymentMethodId) {
            this.paymentMethodId = paymentMethodId;
        }

        public String getMethodCode() {
            return methodCode;
        }

        public void setMethodCode(String methodCode) {
            this.methodCode = methodCode;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }
    }

    public static class StaffInfo {
        private int staffId;
        private String fullName;

        public int getStaffId() {
            return staffId;
        }

        public void setStaffId(int staffId) {
            this.staffId = staffId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }

    // Getters and setters
    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public PatientInfo getPatient() {
        return patient;
    }

    public void setPatient(PatientInfo patient) {
        this.patient = patient;
    }

    public RecordInfo getRecord() {
        return record;
    }

    public void setRecord(RecordInfo record) {
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

    public PaymentMethodInfo getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodInfo paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public EPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(EPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public StaffInfo getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(StaffInfo issuedBy) {
        this.issuedBy = issuedBy;
    }

    public List<InvoiceServiceDetailResponse> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(List<InvoiceServiceDetailResponse> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public List<InvoiceMedicineDetailResponse> getMedicineDetails() {
        return medicineDetails;
    }

    public void setMedicineDetails(List<InvoiceMedicineDetailResponse> medicineDetails) {
        this.medicineDetails = medicineDetails;
    }
}
