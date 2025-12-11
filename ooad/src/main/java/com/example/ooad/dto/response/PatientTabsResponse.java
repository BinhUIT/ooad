package com.example.ooad.dto.response;

import java.util.List;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.MedicalRecord;

public class PatientTabsResponse {
    private List<Appointment> appointments;
    private List<MedicalRecord> medicalRecords;
    public PatientTabsResponse() {
         
    }
    public PatientTabsResponse(List<Appointment> appointments, List<MedicalRecord> medicalRecords) {
        this.appointments= appointments;
        this.medicalRecords = medicalRecords;
    } 
    public List<Appointment> getAppointments() {
        return this.appointments;
    } 
    public List<MedicalRecord> getMedicalRecords(){
        return this.medicalRecords;
    } 
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    } 
    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }
}
