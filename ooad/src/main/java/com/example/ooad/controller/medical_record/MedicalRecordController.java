package com.example.ooad.controller.medical_record;

import java.sql.Date;
import java.util.ArrayList;
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
import com.example.ooad.domain.entity.Prescription;
import com.example.ooad.domain.entity.PrescriptionDetail;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.Service;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.dto.request.CreateMedicalRecordRequest;
import com.example.ooad.dto.request.UpdateMedicalRecordRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.MedicalRecordDetailResponse;
import com.example.ooad.dto.response.PatientDto;
import com.example.ooad.dto.response.DiseaseTypeDto;
import com.example.ooad.dto.response.OrderedServiceDto;
import com.example.ooad.dto.response.PrescriptionDto;
import com.example.ooad.dto.response.PrescriptionDetailDto;
import com.example.ooad.dto.response.MedicineDto;
import com.example.ooad.service.medical_record.interfaces.MedicalRecordService;
import com.example.ooad.service.prescription.interfaces.PrescriptionService;
import com.example.ooad.repository.ServiceRepository;
import com.example.ooad.utils.Message;

import jakarta.validation.Valid;

@RestController
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    private final PrescriptionService prescriptionService;
    private final ServiceRepository serviceRepo;

    public MedicalRecordController(MedicalRecordService medicalRecordService,
            PrescriptionService prescriptionService,
            ServiceRepository serviceRepo) {
        this.medicalRecordService = medicalRecordService;
        this.prescriptionService = prescriptionService;
        this.serviceRepo = serviceRepo;
    }

    // Common endpoints - receptionist and admin get entity
    @GetMapping({ "/receptionist/medical-records/{medicalRecordId}",
            "/admin/medical-records/{medicalRecordId}" })
    public ResponseEntity<GlobalResponse<MedicalRecord>> getMedicalRecordById(@PathVariable int medicalRecordId) {
        MedicalRecord result = medicalRecordService.findMedicalRecordById(medicalRecordId);
        GlobalResponse<MedicalRecord> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/patient/medical-records/{medicalRecordId}")
    public ResponseEntity<GlobalResponse<MedicalRecordDetailResponse>> getMedicalRecordByIdForPatient(
            @PathVariable int medicalRecordId) {
        MedicalRecord record = medicalRecordService.findMedicalRecordById(medicalRecordId);
        MedicalRecordDetailResponse dto = buildDetailResponse(record);
        GlobalResponse<MedicalRecordDetailResponse> response = new GlobalResponse<>(dto, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/doctor/medical-records/{medicalRecordId}")
    public ResponseEntity<GlobalResponse<MedicalRecordDetailResponse>> getMedicalRecordByIdForDoctor(
            @PathVariable int medicalRecordId) {
        MedicalRecord record = medicalRecordService.findMedicalRecordById(medicalRecordId);
        MedicalRecordDetailResponse dto = buildDetailResponse(record);
        GlobalResponse<MedicalRecordDetailResponse> response = new GlobalResponse<>(dto, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private MedicalRecordDetailResponse buildDetailResponse(MedicalRecord record) {
        MedicalRecordDetailResponse dto = new MedicalRecordDetailResponse();
        dto.setRecordId(record.getRecordId());

        Reception reception = record.getReception();
        if (reception != null) {
            dto.setReceptionId(reception.getReceptionId());
            dto.setReceptionStatus(reception.getStatus() != null
                    ? reception.getStatus().name()
                    : null);
            if (reception.getPatient() != null) {
                Patient p = reception.getPatient();
                PatientDto pd = new PatientDto();
                pd.setPatientId(p.getPatientId());
                pd.setFullName(p.getFullName());
                pd.setDateOfBirth(p.getDateOfBirth());
                pd.setGender(p.getGender() != null ? p.getGender().name() : null);
                pd.setAddress(p.getAddress());
                pd.setPhone(p.getPhone());
                pd.setEmail(p.getEmail());
                pd.setIdCard(p.getIdCard());
                pd.setFirstVisitDate(p.getFirstVisitDate());
                dto.setPatient(pd);
            }
        }

        dto.setDoctorId(record.getDoctor() != null ? record.getDoctor().getStaffId() : null);
        dto.setDoctorName(record.getDoctor() != null ? record.getDoctor().getFullName() : null);
        dto.setExaminateDate(record.getExaminateDate());
        dto.setSymptoms(record.getSymptoms());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setNotes(record.getNotes());

        if (record.getDiseaseType() != null) {
            DiseaseTypeDto dt = new DiseaseTypeDto();
            dt.setDiseaseTypeId(record.getDiseaseType().getDiseaseTypeId());
            dt.setDiseaseCode(record.getDiseaseType().getDiseaseCode());
            dt.setDiseaseName(record.getDiseaseType().getDiseaseName());
            dto.setDiseaseType(dt);
        }

        List<OrderedServiceDto> servicesList = new ArrayList<>();
        if (record.getOrderedServices() != null && !record.getOrderedServices().isBlank()) {
            String[] tokens = record.getOrderedServices().split(",");
            for (String t : tokens) {
                String token = t.trim();
                if (token.isEmpty())
                    continue;
                Integer sid = null;
                Integer qty = 1;
                if (token.contains(":")) {
                    String[] parts = token.split(":");
                    try {
                        sid = Integer.valueOf(parts[0].trim());
                        qty = Integer.valueOf(parts[1].trim());
                    } catch (Exception e) {
                        continue;
                    }
                } else {
                    try {
                        sid = Integer.valueOf(token);
                    } catch (Exception e) {
                        continue;
                    }
                }
                String sName = null;
                if (sid != null) {
                    java.util.Optional<Service> sOpt = serviceRepo.findById(sid);
                    if (sOpt.isPresent())
                        sName = sOpt.get().getServiceName();
                }
                servicesList.add(new OrderedServiceDto(sid, sName, qty));
            }
        }
        dto.setOrderedServices(servicesList);

        Prescription pres = prescriptionService.getPrescriptionByRecordId(record.getRecordId());
        if (pres != null) {
            PrescriptionDto presDto = new PrescriptionDto();
            presDto.setPrescriptionId(pres.getPrescriptionId());
            presDto.setPrescriptionDate(pres.getPrescriptionDate());
            presDto.setNotes(pres.getNotes());
            List<PrescriptionDetail> details = prescriptionService
                    .getPrescriptionDetailOfPrescription(pres.getPrescriptionId());
            List<PrescriptionDetailDto> detailDtos = new ArrayList<>();
            if (details != null) {
                for (PrescriptionDetail d : details) {
                    PrescriptionDetailDto dd = new PrescriptionDetailDto();
                    MedicineDto md = new MedicineDto();
                    if (d.getMedicine() != null) {
                        md.setMedicineId(d.getMedicine().getMedicineId());
                        md.setMedicineName(d.getMedicine().getMedicineName());
                        md.setUnit(d.getMedicine().getUnit() != null ? d.getMedicine().getUnit().name() : null);
                    }
                    dd.setMedicine(md);
                    dd.setQuantity(d.getQuantity());
                    dd.setDosage(d.getDosage());
                    dd.setDays(d.getDays());
                    dd.setDispenseStatus(d.getDispenseStatus() != null ? d.getDispenseStatus().name() : "PENDING");
                    detailDtos.add(dd);
                }
            }
            presDto.setPrescriptionDetail(detailDtos);
            dto.setPrescription(presDto);
        }

        return dto;
    }

    // Doctor endpoints
    @GetMapping("/doctor/medical-records")
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
    public ResponseEntity<GlobalResponse<MedicalRecordDetailResponse>> createMedicalRecord(
            @Valid @RequestBody CreateMedicalRecordRequest request,
            BindingResult bindingResult,
            Authentication auth) {
        MedicalRecord result = medicalRecordService.createMedicalRecord(request, bindingResult, auth);

        MedicalRecordDetailResponse dto = buildDetailResponse(result);

        GlobalResponse<MedicalRecordDetailResponse> response = new GlobalResponse<>(dto, Message.success, 200);
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
    @GetMapping("/patient/medical-records")
    public ResponseEntity<GlobalResponse<List<MedicalRecord>>> getMedicalRecordsOfPatient(Authentication auth) {
        List<MedicalRecord> result = medicalRecordService.getMedicalRecordsOfPatient(auth);
        GlobalResponse<List<MedicalRecord>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
