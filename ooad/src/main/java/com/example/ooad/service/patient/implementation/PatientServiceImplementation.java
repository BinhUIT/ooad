package com.example.ooad.service.patient.implementation;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.dto.request.PatientRequest;
import com.example.ooad.dto.response.PatientResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.mapper.PatientMapper;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.MedicalRecordRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.ReceptionRepository;
import com.example.ooad.service.auth.interfaces.AuthService;
import com.example.ooad.service.patient.interfaces.PatientService;
import com.example.ooad.utils.DateTimeUtil;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.ActorValidator;

import jakarta.transaction.Transactional;


@Service
public class PatientServiceImplementation implements PatientService {
    private final PatientRepository patientRepo;
    private final ActorValidator actorValidator;
    private final AppointmentRepository appointmentRepo;
    private final ReceptionRepository receptionRepo;
    private final InvoiceRepository invoiceRepo;
    private final MedicalRecordRepository medicalRecordRepo;
    private final AuthService authService;

    public PatientServiceImplementation(PatientRepository patientRepo, ActorValidator actorValidator,MedicalRecordRepository medicalRecordRepo,
         AppointmentRepository appointmentRepo, ReceptionRepository receptionRepo,
         InvoiceRepository invoiceRepo, AuthService authService ) {
        this.patientRepo = patientRepo;
        this.actorValidator = actorValidator;
        this.appointmentRepo= appointmentRepo;
        this.receptionRepo= receptionRepo;
        this.invoiceRepo = invoiceRepo;
        this.medicalRecordRepo= medicalRecordRepo;
        this.authService= authService;
    }
    private void validateRequest(PatientRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
       }
       if(!actorValidator.validateEmail(request.getEmail())) {
            throw new BadRequestException(Message.invalidEmail);
       }
       if(!actorValidator.validatePhoneNumber(request.getPhone())) {
            throw new BadRequestException(Message.invalidPhone);
       } 
       if(!actorValidator.validateIdCard(request.getIdCard())) {
            throw new BadRequestException(Message.invalidIdCard);
       }
    }
    @Override
    public PatientResponse createPatient(PatientRequest request, BindingResult bindingResult) {
       validateRequest(request, bindingResult);
       Patient newPatient = PatientMapper.fromRequestToPatient(request);
       newPatient.setRecordCreateDate(DateTimeUtil.getCurrentDate());
       newPatient= patientRepo.save(newPatient);
       return PatientMapper.getResponseFromPatient(newPatient);

    }
    @Override
    public PatientResponse updatePatient(PatientRequest request, BindingResult bindingResult, int patientId) {
        validateRequest(request, bindingResult);
        Patient p = findPatientById(patientId);
        PatientMapper.fillInfoToPatient(request, p);
        p=patientRepo.save(p);
        return PatientMapper.getResponseFromPatient(p);
    }
    @Override
    public List<PatientResponse> getAllPatients() {
        List<Patient> listPatient = patientRepo.findAll();
        return listPatient.stream().map(item->PatientMapper.getResponseFromPatient(item)).toList();
    } 
    @Override
    public PatientResponse getPatientById(int patientId) {
        
        return PatientMapper.getResponseFromPatient(findPatientById(patientId));

    }
    public Patient findPatientById(int patientId) {
        Patient p = patientRepo.findById(patientId).orElse(null);
        if(p==null) {
            throw new NotFoundException(Message.patientNotFound);
        }
        return p;
    }
    @Override
    @Transactional
    public void deletePatient(int patientId) {
        Patient p = findPatientById(patientId);
        List<Appointment> appointments = appointmentRepo.findByPatient_PatientId(p.getPatientId());
        for(Appointment a: appointments) {
            a.setPatient(null);
        }
        List<Reception> receptions = receptionRepo.findByPatient_PatientId(p.getPatientId());
        for(Reception r: receptions) {
            if(r.getStatus()==EReceptionStatus.IN_EXAMINATION||r.getStatus()==EReceptionStatus.WAITING) {
                throw new BadRequestException(Message.cannotDeletePatient);
            }
            r.setPatient(null);
        }
        List<Invoice> invoices = invoiceRepo.findByPatient_PatientId(p.getPatientId());
        for(Invoice i: invoices) {
            i.setPatient(null);
        }
        appointmentRepo.saveAll(appointments);
        receptionRepo.saveAll(receptions);
        invoiceRepo.saveAll(invoices);
        patientRepo.delete(p);
    }

    @Override
    public PatientResponse findPatientByIdCard(String idCard) {
        Patient patient = patientRepo.findByIdCard(idCard);
        if(patient==null) {
            throw new NotFoundException(Message.patientNotFound);
        } 
        return PatientMapper.getResponseFromPatient(patient);
    }

    @Override
    public List<Appointment> getAppointmentsOfPatient(int patientId) {
        Patient p = findPatientById(patientId);
        return appointmentRepo.findByPatient_PatientId(p.getPatientId());
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsOfPatient(int patientId) {
        Patient p = findPatientById(patientId);
        List<Reception> receptions = receptionRepo.findByPatient_PatientId(p.getPatientId());
        List<Integer> receptioIds = receptions.stream().map(item->item.getReceptionId()).toList();
        return medicalRecordRepo.findByReception_ReceptionIdIn(receptioIds);
    }

    @Override
    public List<Invoice> getInvoiceOfPatient(int patientId) {
        Patient p = findPatientById(patientId);
        return invoiceRepo.findByPatient_PatientId(p.getPatientId());
    }

    @Override
    public Patient getPatientFromAuth(Authentication auth) {
        Account account = authService.getAccountFromAuth(auth);
        Patient p = patientRepo.findByAccountId(account.getAccountId()).orElseThrow(()->new NotFoundException(Message.patientNotFound));
        return p;
    }

    @Override
    public PatientResponse getPatientResponseFromAuth(Authentication auth) {
        return PatientMapper.getResponseFromPatient(this.getPatientFromAuth(auth));
    }

    
    
}
