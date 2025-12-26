package com.example.ooad.service.prescription.interfaces;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.ooad.domain.entity.Prescription;
import com.example.ooad.domain.entity.PrescriptionDetail;
import com.example.ooad.dto.request.PrescriptionRequest;

public interface PrescriptionService {
    public Page<Prescription> getAllPrescription(int pageNumber, int pageSize, Optional<Date> prescriptionDate);

    public Prescription getPrescriptionById(int prescriptionId);

    public Prescription getPrescriptionByRecordId(int recordId);

    public Page<PrescriptionDetail> getPrescriptionDetailOfPrescription(int pageNumber, int pageSize,
            int prescriptionId);

    public Prescription createPrescription(PrescriptionRequest request);

    public Prescription updatePrescription(PrescriptionRequest request, int prescriptionId);
}
