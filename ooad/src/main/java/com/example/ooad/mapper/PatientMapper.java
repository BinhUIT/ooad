package com.example.ooad.mapper;

import com.example.ooad.domain.entity.Patient;
import com.example.ooad.dto.request.CreatePatientRequest;

public class PatientMapper {
    public static Patient fromRequestToPatient(CreatePatientRequest request) {
        Patient p = new Patient();
        p.setAddress(request.getAddress());
        p.setFirstVisitDate(request.getFirstVisitDate());
        p.setFullName(request.getFullName());
        p.setDateOfBirth(request.getDateOfBirth());
        p.setGender(request.getGender());
        p.setEmail(request.getEmail());
        p.setPhone(request.getPhone());
        p.setIdCard(request.getIdCard());
        return p;
    }
}
