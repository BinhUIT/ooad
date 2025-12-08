package com.example.ooad.service.patient.implementation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Patient;
import com.example.ooad.dto.request.PatientRequest;
import com.example.ooad.dto.response.PatientResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.mapper.PatientMapper;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.service.patient.interfaces.PatientService;
import com.example.ooad.utils.DateTimeUtil;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.ActorValidator;


@Service
public class PatientServiceImplementation implements PatientService {
    private final PatientRepository patientRepo;
    private final ActorValidator actorValidator;
    public PatientServiceImplementation(PatientRepository patientRepo, ActorValidator actorValidator) {
        this.patientRepo = patientRepo;
        this.actorValidator = actorValidator;
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
    private Patient findPatientById(int patientId) {
        Patient p = patientRepo.findById(patientId).orElse(null);
        if(p==null) {
            throw new NotFoundException(Message.patientNotFound);
        }
        return p;
    }
    @Override
    public void deletePatient(int patientId) {
        Patient p = findPatientById(patientId);
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
    
}
