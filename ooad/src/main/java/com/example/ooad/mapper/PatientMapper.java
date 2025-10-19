package com.example.ooad.mapper;

import com.example.ooad.domain.entity.Patient;
import com.example.ooad.dto.request.PatientRequest;
import com.example.ooad.dto.response.PatientResponse;
import com.example.ooad.utils.Message;

public class PatientMapper {
    public static void fillInfoToPatient(PatientRequest request, Patient p) {
        p.setAddress(request.getAddress());
        p.setFirstVisitDate(request.getFirstVisitDate());
        p.setFullName(request.getFullName());
        p.setDateOfBirth(request.getDateOfBirth());
        p.setGender(request.getGender());
        p.setEmail(request.getEmail());
        p.setPhone(request.getPhone());
        p.setIdCard(request.getIdCard());
    }
    public static Patient fromRequestToPatient(PatientRequest request) {
        if(request==null) {
            throw new RuntimeException(Message.nullArgument);
        }
        Patient p = new Patient();
        fillInfoToPatient(request, p);
        return p;
    }
    public static PatientResponse getResponseFromPatient(Patient patient) {
        if(patient==null) {
            throw new RuntimeException(Message.nullArgument);
        }
        return new PatientResponse(patient.getPatientId(), patient.getAddress(), patient.getFirstVisitDate(), patient.getFullName(), patient.getDateOfBirth(), patient.getGender()
        , patient.getEmail(), patient.getPhone(), patient.getIdCard());
    }
    
}
