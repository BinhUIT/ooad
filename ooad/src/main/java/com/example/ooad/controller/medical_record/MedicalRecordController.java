package com.example.ooad.controller.medical_record;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.dto.request.CreateMedicalRecordRequest;
import com.example.ooad.dto.request.UpdateMedicalRecordRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.medical_record.interfaces.MedicalRecordService;
import com.example.ooad.utils.Message;

import jakarta.validation.Valid;

@RestController
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // Common endpoints
    @GetMapping({ "/receptionist/medical_record_by_id/{medicalRecordId}",
            "/admin/medical_record_by_id/{medicalRecordId}", "/doctor/medical_record_by_id/{medicalRecordId}",
            "/patient/medical_record_by_id/{medicalRecordId}" })
    public ResponseEntity<GlobalResponse<MedicalRecord>> getMedicalRecordById(@PathVariable int medicalRecordId) {
        MedicalRecord result = medicalRecordService.findMedicalRecordById(medicalRecordId);
        GlobalResponse<MedicalRecord> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Doctor endpoints
    @GetMapping("/doctor/medical_records")
    public ResponseEntity<GlobalResponse<Page<MedicalRecord>>> getListMedicalRecordsForDoctor(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam Optional<Date> date,
            @RequestParam Optional<Integer> receptionId) {
        Page<MedicalRecord> result = medicalRecordService.getListMedicalRecordsForDoctor(pageNumber, pageSize, date,
                receptionId);
        GlobalResponse<Page<MedicalRecord>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/doctor/medical_record/create")
    public ResponseEntity<GlobalResponse<MedicalRecord>> createMedicalRecord(
            @Valid @RequestBody CreateMedicalRecordRequest request,
            BindingResult bindingResult,
            Authentication auth) {
        MedicalRecord result = medicalRecordService.createMedicalRecord(request, bindingResult, auth);
        GlobalResponse<MedicalRecord> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/doctor/medical_record/update/{medicalRecordId}")
    public ResponseEntity<GlobalResponse<MedicalRecord>> updateMedicalRecord(
            @RequestBody UpdateMedicalRecordRequest request,
            BindingResult bindingResult,
            @PathVariable int medicalRecordId,
            Authentication auth) {
        MedicalRecord result = medicalRecordService.updateMedicalRecord(request, bindingResult, medicalRecordId, auth);
        GlobalResponse<MedicalRecord> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Patient endpoints
    @GetMapping("/patient/medical_records")
    public ResponseEntity<GlobalResponse<List<MedicalRecord>>> getMedicalRecordsOfPatient(Authentication auth) {
        List<MedicalRecord> result = medicalRecordService.getMedicalRecordsOfPatient(auth);
        GlobalResponse<List<MedicalRecord>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
