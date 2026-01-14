package com.example.ooad.dto.response;

public class PatientVisitStatistic {
    private int totalNewPatients;
    private int totalReturningPatients;
    private int totalVisits;

    public PatientVisitStatistic() {
    }

    public PatientVisitStatistic(int totalNewPatients, int totalReturningPatients, int totalVisits) {
        this.totalNewPatients = totalNewPatients;
        this.totalReturningPatients = totalReturningPatients;
        this.totalVisits = totalVisits;
    }

    public int getTotalNewPatients() {
        return totalNewPatients;
    }

    public void setTotalNewPatients(int totalNewPatients) {
        this.totalNewPatients = totalNewPatients;
    }

    public int getTotalReturningPatients() {
        return totalReturningPatients;
    }

    public void setTotalReturningPatients(int totalReturningPatients) {
        this.totalReturningPatients = totalReturningPatients;
    }

    public int getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(int totalVisits) {
        this.totalVisits = totalVisits;
    }
}
