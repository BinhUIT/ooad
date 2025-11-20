package com.example.ooad.service.patient.interfaces;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.example.ooad.dto.request.PatientRequest;
import com.example.ooad.dto.response.PatientResponse;

public interface PatientService {
    public PatientResponse createPatient(PatientRequest request, BindingResult bindingResult);
    public PatientResponse updatePatient(PatientRequest request, BindingResult bindingResult, int patientId);
    public List<PatientResponse> getAllPatients();
    public PatientResponse getPatientById(int patientId);
    public void deletePatient(int patientId);
}
