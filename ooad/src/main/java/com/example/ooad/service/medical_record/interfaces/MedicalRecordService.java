package com.example.ooad.service.medical_record.interfaces;

import com.example.ooad.domain.entity.MedicalRecord;

public interface  MedicalRecordService {
    public MedicalRecord findMedicalRecordById(int recordId);
}
