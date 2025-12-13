package com.example.ooad.controller.medical_record;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.medical_record.interfaces.MedicalRecordService;
import com.example.ooad.utils.Message;

@RestController
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }
    @GetMapping({"/receptionist/medical_record_by_id/{medicalRecordId}","/admin/medical_record_by_id/{medicalRecordId}"})
    public ResponseEntity<GlobalResponse<MedicalRecord>> getMedicalRecordById(@PathVariable int medicalRecordId) {
        MedicalRecord result = medicalRecordService.findMedicalRecordById(medicalRecordId);
        GlobalResponse<MedicalRecord> response = new GlobalResponse<>(result,Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
