package com.example.ooad.service.medical_record.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.InvoiceServiceDetail;
import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.RefDiseaseType;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.SysParam;
import com.example.ooad.domain.enums.EPaymentStatus;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.dto.request.CreateMedicalRecordRequest;
import com.example.ooad.dto.request.UpdateMedicalRecordRequest;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.InvoiceServiceDetailRepository;
import com.example.ooad.repository.MedicalRecordRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.RefDiseaseTypeRepository;
import com.example.ooad.repository.ReceptionRepository;
import com.example.ooad.repository.ServiceRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.repository.SysParamRepository;
import com.example.ooad.service.auth.interfaces.AuthService;
import com.example.ooad.service.medical_record.interfaces.MedicalRecordService;
import com.example.ooad.service.reception.interfaces.ReceptionService;
import com.example.ooad.utils.Message;

import jakarta.transaction.Transactional;

@Service
public class MedicalRecordServiceImplementation implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepo;
    private final ReceptionService receptionService;
    private final AuthService authService;
    private final StaffRepository staffRepo;
    private final RefDiseaseTypeRepository refDiseaseTypeRepo;
    private final InvoiceRepository invoiceRepo;
    private final PatientRepository patientRepo;
    private final ReceptionRepository receptionRepo;
    private final InvoiceServiceDetailRepository invoiceServiceDetailRepo;
    private final ServiceRepository serviceRepo;
    private final SysParamRepository sysParamRepo;

    public MedicalRecordServiceImplementation(
            MedicalRecordRepository medicalRecordRepo,
            ReceptionService receptionService,
            AuthService authService,
            StaffRepository staffRepo,
            RefDiseaseTypeRepository refDiseaseTypeRepo,
            InvoiceRepository invoiceRepo,
            PatientRepository patientRepo,
            ReceptionRepository receptionRepo,
            InvoiceServiceDetailRepository invoiceServiceDetailRepo,
            ServiceRepository serviceRepo,
            SysParamRepository sysParamRepo) {
        this.medicalRecordRepo = medicalRecordRepo;
        this.receptionService = receptionService;
        this.authService = authService;
        this.staffRepo = staffRepo;
        this.refDiseaseTypeRepo = refDiseaseTypeRepo;
        this.invoiceRepo = invoiceRepo;
        this.patientRepo = patientRepo;
        this.receptionRepo = receptionRepo;
        this.invoiceServiceDetailRepo = invoiceServiceDetailRepo;
        this.serviceRepo = serviceRepo;
        this.sysParamRepo = sysParamRepo;
    }

    @Override
    public MedicalRecord findMedicalRecordById(int recordId) {
        return medicalRecordRepo.findById(recordId)
                .orElseThrow(() -> new NotFoundException(Message.medicalRecordNotFound));
    }

    @Override
    @Transactional
    public MedicalRecord createMedicalRecord(CreateMedicalRecordRequest request, BindingResult bindingResult,
            Authentication auth) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Get doctor from authentication
        Account account = authService.getAccountFromAuth(auth);
        if (account.getRole() != ERole.DOCTOR) {
            throw new BadRequestException("Only doctors can create medical records");
        }
        Staff doctor = staffRepo.findByAccountId(account.getAccountId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        // Get and validate reception
        Reception reception = receptionService.getReceptionById(request.getReceptionId());
        if (reception.getStatus() == EReceptionStatus.DONE || reception.getStatus() == EReceptionStatus.CANCELLED) {
            throw new BadRequestException("Cannot create medical record for completed or cancelled reception");
        }

        // Create medical record
        MedicalRecord record = new MedicalRecord();
        record.setReception(reception);
        record.setDoctor(doctor);
        record.setExaminateDate(
                request.getExaminateDate() != null ? request.getExaminateDate() : Date.valueOf(LocalDate.now()));
        record.setSymptoms(request.getSymptoms());
        record.setDiagnosis(request.getDiagnosis());
        record.setOrderedServices(request.getOrderedServices());
        record.setNotes(request.getNotes());

        // Set disease type if provided
        if (request.getDiseaseTypeId() != null) {
            RefDiseaseType diseaseType = refDiseaseTypeRepo.findById(request.getDiseaseTypeId())
                    .orElseThrow(() -> new NotFoundException("Disease type not found"));
            record.setDiseaseType(diseaseType);
        }

        record = medicalRecordRepo.save(record);

        // Update reception status to IN_EXAMINATION after creating medical record
        reception.setStatus(EReceptionStatus.IN_EXAMINATION);
        receptionRepo.save(reception);

        // Create invoice if requested
        if (request.getCreateInvoice() != null && request.getCreateInvoice()) {
            Invoice invoice = new Invoice();
            invoice.setPatient(reception.getPatient());
            invoice.setRecord(record);
            invoice.setInvoiceDate(Date.valueOf(LocalDate.now()));

            // Get examination fee from system parameter
            BigDecimal examFee = getExaminationFee();
            invoice.setExaminationFee(examFee);
            invoice.setMedicineFee(new BigDecimal(0));
            invoice.setServiceFee(new BigDecimal(0));
            invoice.setTotalAmount(examFee);
            invoice.setPaymentStatus(EPaymentStatus.UNPAID);
            invoice.setIssueBy(doctor);
            invoiceRepo.save(invoice);
        }

        return record;
    }

    @Override
    @Transactional
    public MedicalRecord updateMedicalRecord(UpdateMedicalRecordRequest request, BindingResult bindingResult,
            int recordId, Authentication auth) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Get doctor from authentication
        Account account = authService.getAccountFromAuth(auth);
        if (account.getRole() != ERole.DOCTOR) {
            throw new BadRequestException("Only doctors can update medical records");
        }

        MedicalRecord record = findMedicalRecordById(recordId);

        // Check if invoice is paid - cannot update paid medical records
        Optional<Invoice> invoiceOpt = invoiceRepo.findByRecord_RecordId(recordId);
        if (invoiceOpt.isPresent() && invoiceOpt.get().getPaymentStatus() == EPaymentStatus.PAID) {
            throw new BadRequestException("Cannot update medical record - invoice has been paid");
        }

        // Update fields if provided
        if (request.getExaminateDate() != null) {
            record.setExaminateDate(request.getExaminateDate());
        }
        if (request.getSymptoms() != null) {
            record.setSymptoms(request.getSymptoms());
        }
        if (request.getDiagnosis() != null) {
            record.setDiagnosis(request.getDiagnosis());
        }
        if (request.getOrderedServices() != null) {
            record.setOrderedServices(request.getOrderedServices());
        }
        if (request.getNotes() != null) {
            record.setNotes(request.getNotes());
        }
        if (request.getDiseaseTypeId() != null) {
            RefDiseaseType diseaseType = refDiseaseTypeRepo.findById(request.getDiseaseTypeId())
                    .orElseThrow(() -> new NotFoundException("Disease type not found"));
            record.setDiseaseType(diseaseType);
        }

        MedicalRecord savedRecord = medicalRecordRepo.save(record);

        // Sync invoice service details if ordered services changed
        if (request.getOrderedServices() != null) {
            syncInvoiceServiceDetails(savedRecord);
        }

        return savedRecord;
    }

    @Override
    public Page<MedicalRecord> getListMedicalRecordsForDoctor(int pageNumber, int pageSize, Optional<Date> date,
            Optional<Integer> receptionId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "examinateDate"));

        if (receptionId.isPresent()) {
            return medicalRecordRepo.findByReception_ReceptionId(receptionId.get(), pageable);
        }

        if (date.isPresent()) {
            return medicalRecordRepo.findByExaminateDate(date.get(), pageable);
        }

        return medicalRecordRepo.findAll(pageable);
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsOfPatient(Authentication auth) {
        Account account = authService.getAccountFromAuth(auth);

        // Validate user role first
        if (account.getRole() != ERole.PATIENT) {
            throw new BadRequestException("Only patients can view their medical records");
        }

        Patient patient = patientRepo.findByAccountId(account.getAccountId())
                .orElseThrow(() -> new NotFoundException(Message.patientNotFound));

        List<Reception> receptions = receptionRepo.findByPatient_PatientId(patient.getPatientId());
        List<Integer> receptionIds = receptions.stream().map(Reception::getReceptionId).toList();

        return medicalRecordRepo.findByReception_ReceptionIdIn(receptionIds);
    }

    @Override
    public List<MedicalRecord> findAllRecords() {
        return medicalRecordRepo.findAllByOrderByRecordIdDesc();
    }

    private BigDecimal getExaminationFee() {
        try {
            Optional<SysParam> examFeeParam = sysParamRepo.findByParamCode("EXAM_FEE");
            if (examFeeParam.isPresent() && examFeeParam.get().isActive()) {
                String value = examFeeParam.get().getParamValue();
                return new BigDecimal(value);
            }
        } catch (Exception e) {
        }
        return new BigDecimal("100000");
    }

    private void syncInvoiceServiceDetails(MedicalRecord record) {
        Optional<Invoice> invoiceOpt = invoiceRepo.findByRecord_RecordId(record.getRecordId());
        if (!invoiceOpt.isPresent()) {
            return;
        }

        Invoice invoice = invoiceOpt.get();

        invoiceServiceDetailRepo.deleteByInvoiceId(invoice.getInvoiceId());

        String orderedServices = record.getOrderedServices();
        if (orderedServices != null && !orderedServices.isBlank()) {
            String[] tokens = orderedServices.split(",");
            for (String token : tokens) {
                token = token.trim();
                if (token.isEmpty())
                    continue;

                Integer serviceId = null;
                Integer quantity = 1;

                if (token.contains(":")) {
                    String[] parts = token.split(":");
                    try {
                        serviceId = Integer.valueOf(parts[0].trim());
                        quantity = Integer.valueOf(parts[1].trim());
                    } catch (Exception e) {
                        continue;
                    }
                } else {
                    try {
                        serviceId = Integer.valueOf(token);
                    } catch (Exception e) {
                        continue;
                    }
                }

                if (serviceId != null) {
                    Optional<com.example.ooad.domain.entity.Service> serviceOpt = serviceRepo.findById(serviceId);
                    if (serviceOpt.isPresent()) {
                        com.example.ooad.domain.entity.Service service = serviceOpt.get();
                        BigDecimal salePrice = service.getUnitPrice();
                        BigDecimal amount = salePrice.multiply(BigDecimal.valueOf(quantity));

                        InvoiceServiceDetail detail = new InvoiceServiceDetail();
                        detail.setInvoice(invoice);
                        detail.setService(service);
                        detail.setQuantity(quantity);
                        detail.setSalePrice(salePrice);
                        detail.setAmount(amount);
                        invoiceServiceDetailRepo.save(detail);
                    }
                }
            }
        }

        recalculateInvoice(invoice);
    }

    private void recalculateInvoice(Invoice invoice) {
        List<InvoiceServiceDetail> serviceDetails = invoiceServiceDetailRepo
                .findByInvoice_InvoiceId(invoice.getInvoiceId());
        BigDecimal serviceFee = serviceDetails.stream()
                .map(InvoiceServiceDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        invoice.setServiceFee(serviceFee);
        invoice.setTotalAmount(invoice.getExaminationFee().add(invoice.getMedicineFee()).add(serviceFee));
        invoiceRepo.save(invoice);
    }

}
