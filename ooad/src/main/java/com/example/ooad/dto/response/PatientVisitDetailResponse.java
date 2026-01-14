package com.example.ooad.dto.response;

import java.sql.Date;

public class PatientVisitDetailResponse {
    private int patientId;
    private String patientName;
    private Date firstVisitDate;
    private int totalVisits;
    private boolean isNewPatient;

    public PatientVisitDetailResponse() {
    }

    public PatientVisitDetailResponse(int patientId, String patientName, Date firstVisitDate, int totalVisits,
            boolean isNewPatient) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.firstVisitDate = firstVisitDate;
        this.totalVisits = totalVisits;
        this.isNewPatient = isNewPatient;
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

    public Date getFirstVisitDate() {
        return firstVisitDate;
    }

    public void setFirstVisitDate(Date firstVisitDate) {
        this.firstVisitDate = firstVisitDate;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(int totalVisits) {
        this.totalVisits = totalVisits;
    }

    public boolean isNewPatient() {
        return isNewPatient;
    }

    public void setNewPatient(boolean newPatient) {
        isNewPatient = newPatient;
    }
}
