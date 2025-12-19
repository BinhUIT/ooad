package com.example.ooad.service.prescription.implementation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.Prescription;
import com.example.ooad.domain.entity.PrescriptionDetail;
import com.example.ooad.dto.request.PrescriptionDetailRequest;
import com.example.ooad.dto.request.PrescriptionRequest;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.repository.PrescriptionDetailRepository;
import com.example.ooad.repository.PrescriptionRepository;
import com.example.ooad.service.medical_record.interfaces.MedicalRecordService;
import com.example.ooad.service.prescription.interfaces.PrescriptionService;
import com.example.ooad.utils.Message;

import jakarta.transaction.Transactional;

@Service
public class PrescriptionServiceImplementation implements PrescriptionService{
    private final PrescriptionRepository prescriptionRepo;
    private final PrescriptionDetailRepository prescriptionDetailRepo;
    private final MedicalRecordService medicalRecordService;
    private final MedicineRepository medicineRepo;
    
    public PrescriptionServiceImplementation(PrescriptionRepository prescriptionRepo, PrescriptionDetailRepository prescriptionDetailRepo,
         MedicalRecordService medicalRecordService, MedicineRepository medicineRepo) {
        this.prescriptionRepo=prescriptionRepo;
        this.prescriptionDetailRepo= prescriptionDetailRepo;
        this.medicalRecordService = medicalRecordService;
        this.medicineRepo = medicineRepo;
    }
    @Override
    public Page<Prescription> getAllPrescription(int pageNumber, int pageSize, Optional<Date> prescriptionDate) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if(prescriptionDate.isPresent()) {
            return prescriptionRepo.findByPrescriptionDate(pageable, prescriptionDate.get());
        }
        return prescriptionRepo.findAll(pageable);
    }

    @Override
    public Prescription getPrescriptionById(int prescriptionId) {
       return prescriptionRepo.findById(prescriptionId).orElseThrow(()->new NotFoundException(Message.prescriptionNotFound));
    }

    @Override
    public Page<PrescriptionDetail> getPrescriptionDetailOfPrescription(int pageNumber, int pageSize,
            int prescriptionId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return prescriptionDetailRepo.findByPrescription_PrescriptionId(pageable, prescriptionId);
    }
    @Override
    @Transactional
    public Prescription createPrescription(PrescriptionRequest request) {
        Prescription prescription = new Prescription();
        return fillInfoToPrescription(prescription, request);
    }
    @Override
    @Transactional
    public Prescription updatePrescription(PrescriptionRequest request, int prescriptionId) {
        Prescription prescription = this.getPrescriptionById(prescriptionId);
        return fillInfoToPrescription(prescription, request);
    }
    
    private Prescription fillInfoToPrescription(Prescription prescription, PrescriptionRequest request) {
        clearPrescriptionDetail(prescription);
        prescription.setNotes(request.getNotes());
        prescription.setRecord(medicalRecordService.findMedicalRecordById(request.getRecordId()));
        prescription=prescriptionRepo.save(prescription);
        List<PrescriptionDetail> prescriptionDetails = new ArrayList<>();
        List<Integer> listMedicineIds = request.getPrescriptionDetails().stream().map(item->item.getMedicineId()).toList();
        List<Medicine> medicines = medicineRepo.findByMedicineId_In(listMedicineIds);
        
        for(PrescriptionDetailRequest detailRequest: request.getPrescriptionDetails()) {
            
            prescriptionDetails.add(fromRequestToPrescriptionDetail(prescription, detailRequest, medicines));
        }
        prescriptionDetailRepo.saveAll(prescriptionDetails);
        return prescription;


    }
    private PrescriptionDetail fromRequestToPrescriptionDetail(Prescription prescription, PrescriptionDetailRequest detailRequest, List<Medicine> medicines) {
        if(prescription.getPrescriptionId()!=0&&prescription.getPrescriptionId()!=detailRequest.getPrescriptionId()) {
                throw new BadRequestException(Message.invalidData);
            }
        PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setDays(detailRequest.getDays());
        prescriptionDetail.setDosage(detailRequest.getDosage());
        prescriptionDetail.setQuantity(detailRequest.getQuantity());
        prescriptionDetail.setPrescription(prescription);
        Medicine med = findByMedicineIdInList(medicines, detailRequest.getMedicineId());
        prescriptionDetail.setMedicine(med);
        return prescriptionDetail;
    }
    private Medicine findByMedicineIdInList(List<Medicine> medicines, int medicineId) {
        for(Medicine med: medicines) {
            if(med.getMedicineId()==medicineId) {
                return med;
            }
        }
        throw new NotFoundException(Message.medicineNotFound);
    }
    private void clearPrescriptionDetail(Prescription prescription) {
        List<PrescriptionDetail> prescriptionDetails = prescriptionDetailRepo.findByPrescription_PrescriptionId(prescription.getPrescriptionId());
        prescriptionDetailRepo.deleteAll(prescriptionDetails);
    }
    
}
