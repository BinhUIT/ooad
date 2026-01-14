package com.example.ooad.dto.response;

public class PatientVisitReportData {
    private String month;
    private int newPatients;
    private int returningPatients;
    private int totalVisits;

    public PatientVisitReportData() {
    }

    public PatientVisitReportData(String month, int newPatients, int returningPatients, int totalVisits) {
        this.month = month;
        this.newPatients = newPatients;
        this.returningPatients = returningPatients;
        this.totalVisits = totalVisits;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getNewPatients() {
        return newPatients;
    }

    public void setNewPatients(int newPatients) {
        this.newPatients = newPatients;
    }

    public int getReturningPatients() {
        return returningPatients;
    }

    public void setReturningPatients(int returningPatients) {
        this.returningPatients = returningPatients;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(int totalVisits) {
        this.totalVisits = totalVisits;
    }
}
