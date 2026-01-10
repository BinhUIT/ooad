package com.example.ooad.service.patient.implementation;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Prescription;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.enums.EAppointmentStatus;
import com.example.ooad.domain.enums.EGender;
import com.example.ooad.domain.enums.EPaymentStatus;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.dto.request.PatientRequest;
import com.example.ooad.dto.response.PatientDashboardResponse;
import com.example.ooad.dto.response.PatientResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.mapper.PatientMapper;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.MedicalRecordRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.PrescriptionRepository;
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
    private final AccountRepository accountRepo;
    

    public PatientServiceImplementation(PatientRepository patientRepo, ActorValidator actorValidator,MedicalRecordRepository medicalRecordRepo,
         AppointmentRepository appointmentRepo, ReceptionRepository receptionRepo,
         InvoiceRepository invoiceRepo, AuthService authService , AccountRepository accountRepo) {
        this.patientRepo = patientRepo;
        this.actorValidator = actorValidator;
        this.appointmentRepo= appointmentRepo;
        this.receptionRepo= receptionRepo;
        this.invoiceRepo = invoiceRepo;
        this.medicalRecordRepo= medicalRecordRepo;
        this.authService= authService;
        this.accountRepo = accountRepo;
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
            if(a.getStatus()==EAppointmentStatus.SCHEDULED||a.getStatus()==EAppointmentStatus.CONFIRMED) {
                throw new BadRequestException(Message.cannotDeletePatient);
            }
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
        if(p.getAccount()!=null) {
            accountRepo.delete(p.getAccount());
        }
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

    @Override
    public Page<Patient> searchPatient(int pageNumber, int pageSize, Optional<String> keyword, Optional<EGender> gender) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        String keyWordString = keyword.orElse(null);
        EGender genderParam = gender.orElse(null);
        return patientRepo.searchPatient(pageable, keyWordString, genderParam);
    }
    @Override
    public PatientDashboardResponse getPatientDashboard(Patient patient) {
        PatientDashboardResponse response = new PatientDashboardResponse();
        List<Appointment> appointments = getCurrentAppointments(patient, 2);
        if(appointments!=null&&!appointments.isEmpty()) {
            response.setNextAppointment(appointments.get(0));
        }
        else {
            response.setNextAppointment(null);
        }
        response.setRecentAppointments(appointments);
        response.setMedicalRecordsAmount(medicalRecordRepo.findByPatient_PatientId(patient.getPatientId()).size());
        response.setPendingInvoicesAmount(invoiceRepo.findByPatient_PatientIdAndPaymentStatus(patient.getPatientId(), EPaymentStatus.UNPAID).size());
        return response;
    }

    public List<Appointment> getCurrentAppointments(Patient patient, int size) {
        Date currentDate = Date.valueOf(LocalDate.now());
        LocalTime currentTime = LocalTime.now();
        List<Appointment> appointments = appointmentRepo.findByPatient_PatientId(patient.getPatientId());
        List<Appointment> appointmentAfterCurrent = appointments.stream().filter(item->{
            if(item.getAppointmentDate().after(currentDate)) return true;
            if(item.getAppointmentDate().before(currentDate)) return false;
            return item.getAppointmentTime().isAfter(currentTime);
        }).toList();

        List<Appointment> sortedAppointmentAfterCurrent = appointmentAfterCurrent.stream().sorted(Comparator
        .comparing(Appointment::getAppointmentDate).reversed()
        .thenComparing(Appointment::getAppointmentTime).reversed()).toList();
        
        if(size>=sortedAppointmentAfterCurrent.size()) return sortedAppointmentAfterCurrent;
        List<Appointment> result = sortedAppointmentAfterCurrent;
        for(int i=0;i<size;i++) {
            result.add(sortedAppointmentAfterCurrent.get(i));
        }
        return result;
    }

    

    
    
}
