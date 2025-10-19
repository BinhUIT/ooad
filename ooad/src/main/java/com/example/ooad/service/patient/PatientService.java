package com.example.ooad.service.patient;

import org.springframework.stereotype.Service;

import com.example.ooad.repository.PatientRepository;

@Service
public class PatientService {
    private final PatientRepository patientRepo;
    public PatientService(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }
    
}
