package com.example.ooad.service.prescription.interfaces;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.Prescription;
import com.example.ooad.domain.entity.PrescriptionDetail;
import com.example.ooad.dto.request.PrescriptionRequest;

public interface PrescriptionService {
    public Page<Prescription> getAllPrescription(int pageNumber, int pageSize, Optional<Date> prescriptionDate, Optional<String> patientName);
    public Prescription getPrescriptionById(int prescriptionId);
    public List<PrescriptionDetail> getPrescriptionDetailOfPrescription(int prescriptionId);
    public Prescription createPrescription(PrescriptionRequest request);
    public Prescription updatePrescription(PrescriptionRequest request, int prescriptionId);
    public List<MedicalRecord> getRecords();
    public List<Medicine> getMedicines();
}
