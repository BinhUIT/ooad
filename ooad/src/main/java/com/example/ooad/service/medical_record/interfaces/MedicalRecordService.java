package com.example.ooad.service.medical_record.interfaces;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.example.ooad.domain.entity.MedicalRecord;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

import com.example.ooad.dto.request.CreateMedicalRecordRequest;
import com.example.ooad.dto.request.UpdateMedicalRecordRequest;

public interface MedicalRecordService {
    public MedicalRecord findMedicalRecordById(int recordId);

    // Doctor endpoints
    public MedicalRecord createMedicalRecord(CreateMedicalRecordRequest request, BindingResult bindingResult,
            Authentication auth);

    public MedicalRecord updateMedicalRecord(UpdateMedicalRecordRequest request, BindingResult bindingResult,
            int recordId, Authentication auth);

    public Page<MedicalRecord> getListMedicalRecordsForDoctor(int pageNumber, int pageSize, Optional<Date> date,
            Optional<Integer> receptionId);

    // Patient endpoints
    public List<MedicalRecord> getMedicalRecordsOfPatient(Authentication auth);

    public List<MedicalRecord> findAllRecords();
}
