package com.example.ooad.service.medical_record.implementation;

import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.MedicalRecordRepository;
import com.example.ooad.service.medical_record.interfaces.MedicalRecordService;
import com.example.ooad.utils.Message;
@Service
public class MedicalRecordServiceImplementation implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepo;
    public MedicalRecordServiceImplementation(MedicalRecordRepository medicalRecordRepo) {
        this.medicalRecordRepo = medicalRecordRepo;
    }
    @Override
    public MedicalRecord findMedicalRecordById(int recordId) {
        return medicalRecordRepo.findById(recordId).orElseThrow(()->new NotFoundException(Message.medicalRecordNotFound));
    }
    
}
