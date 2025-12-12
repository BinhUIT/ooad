package com.example.ooad.service.patient.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Patient;
import com.example.ooad.dto.request.PatientRequest;
import com.example.ooad.dto.response.PatientResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.MedicalRecordRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.ReceptionRepository;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.ActorValidator;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplementationTest {
     @Mock
    private PatientRepository patientRepo;
    @Mock
    private ActorValidator actorValidator;
    @Mock
    private AppointmentRepository appointmentRepo;
    @Mock
    private ReceptionRepository receptionRepo;
    @Mock
    private InvoiceRepository invoiceRepo;
    @Mock
    private MedicalRecordRepository medicalRecordRepo;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PatientServiceImplementation patientService;

    private PatientRequest validRequest;
    private Patient savedPatient;
    private int patientId=1;
    
    @BeforeEach
    void setUp() {
        
        validRequest = new PatientRequest();
        validRequest.setFullName("Nguyen Van A");
        validRequest.setEmail("test@example.com");
        validRequest.setPhone("0901234567");
        validRequest.setIdCard("123456789012");
        savedPatient = new Patient();
        savedPatient.setPatientId(1);
        savedPatient.setFullName("Nguyen Van A");
        savedPatient.setEmail("test@example.com");
        savedPatient.setRecordCreateDate(Date.valueOf(LocalDate.now()));
    }

    @Test
    @DisplayName("Create Patient - Success: Should save patient and return response")
    void createPatient_Success() {
        
        when(bindingResult.hasErrors()).thenReturn(false);

        
        when(actorValidator.validateEmail(validRequest.getEmail())).thenReturn(true);
        when(actorValidator.validatePhoneNumber(validRequest.getPhone())).thenReturn(true);
        when(actorValidator.validateIdCard(validRequest.getIdCard())).thenReturn(true);

        
        when(patientRepo.save(any(Patient.class))).thenReturn(savedPatient);

        
        PatientResponse response = patientService.createPatient(validRequest, bindingResult);

        
        assertNotNull(response);
        assertEquals(savedPatient.getPatientId(), response.getPatientId());
        assertEquals(savedPatient.getFullName(), response.getFullName());

        
        verify(patientRepo, times(1)).save(any(Patient.class));
    }

    @Test
    void createPatientFail() {
        when(bindingResult.hasErrors()).thenReturn(false);
        
        when(actorValidator.validateEmail(validRequest.getEmail())).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            patientService.createPatient(validRequest, bindingResult);
        });
        assertEquals(Message.invalidEmail, exception.getMessage());
        verify(patientRepo, never()).save(any(Patient.class));
    }

    @Test
    void updatePatientSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);

        
        when(actorValidator.validateEmail(validRequest.getEmail())).thenReturn(true);
        when(actorValidator.validatePhoneNumber(validRequest.getPhone())).thenReturn(true);
        when(actorValidator.validateIdCard(validRequest.getIdCard())).thenReturn(true);

        when(patientRepo.findById(patientId)).thenReturn(java.util.Optional.of(savedPatient));
        when(patientRepo.save(any(Patient.class))).thenReturn(savedPatient);
        PatientResponse response = patientService.updatePatient(validRequest, bindingResult, patientId);
        assertNotNull(response);
        assertEquals(savedPatient.getPatientId(), response.getPatientId());
        assertEquals(savedPatient.getFullName(), response.getFullName());
        verify(patientRepo).findById(patientId);
        verify(patientRepo).save(any(Patient.class));
    }

    @Test
    void updatePatientInvalidInfo() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(actorValidator.validateEmail(validRequest.getEmail())).thenReturn(false);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            patientService.updatePatient(validRequest, bindingResult, patientId);
        });
        assertEquals(Message.invalidEmail, exception.getMessage());
        verify(patientRepo, never()).save(any(Patient.class));
    }
    @Test
    void updatePatientNotFound() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(actorValidator.validateEmail(validRequest.getEmail())).thenReturn(true);
        when(actorValidator.validatePhoneNumber(validRequest.getPhone())).thenReturn(true);
        when(actorValidator.validateIdCard(validRequest.getIdCard())).thenReturn(true);
        when(patientRepo.findById(patientId)).thenReturn(java.util.Optional.empty());
        NotFoundException notFoundException=assertThrows(NotFoundException.class, () -> {
            patientService.updatePatient(validRequest, bindingResult, patientId);
        });
        assertEquals(notFoundException.getMessage(), Message.patientNotFound);
        verify(patientRepo, never()).save(any(Patient.class));
    }

    @Test
    void getPatientListSuccess() {
        when(patientRepo.findAll()).thenReturn(new ArrayList<>(Arrays.asList(savedPatient)));
        List<PatientResponse> responses = patientService.getAllPatients();
        assertNotNull(responses);
        assertNotEquals(responses.size(), 0);
    }
    @Test
    void getPatientByIdSuccess() {
        when(patientRepo.findById(patientId)).thenReturn(Optional.of(savedPatient));
        PatientResponse response = patientService.getPatientById(patientId);
        assertNotNull(response);
        assertEquals(response.getPatientId(), savedPatient.getPatientId());
    }
    @Test
    void getPatientByIdNotFound() {
        when(patientRepo.findById(patientId)).thenReturn(Optional.empty());
        NotFoundException notFoundException=assertThrows(NotFoundException.class, () -> {
            patientService.findPatientById(patientId);
        });
        assertEquals(notFoundException.getMessage(), Message.patientNotFound);
    }
    @Test
    void deletePatientSuccess() {
        when(patientRepo.findById(patientId)).thenReturn(Optional.of(savedPatient));
        patientService.deletePatient(patientId);
        verify(patientRepo).delete(any(Patient.class));
    }

    @Test
    void deletePatientNotFound() {
        when(patientRepo.findById(patientId)).thenReturn(Optional.empty());
        NotFoundException notFoundException=assertThrows(NotFoundException.class, () -> {
            patientService.deletePatient(patientId);
        });
        assertEquals(notFoundException.getMessage(), Message.patientNotFound);
        verify(patientRepo,never()).delete(any(Patient.class));
    }

    @Test
    void getPatientByIdCardSuccess() {
        String idCard ="23094732";
        when(patientRepo.findByIdCard(idCard)).thenReturn(savedPatient);
        PatientResponse response = patientService.findPatientByIdCard(idCard);
        assertNotNull(response);
        assertEquals(response.getPatientId(), savedPatient.getPatientId());
    }

    @Test
    void getPatientByIdCardNotFound() {
        String idCard ="23094732";
        when(patientRepo.findByIdCard(idCard)).thenReturn(null);
        NotFoundException notFoundException=assertThrows(NotFoundException.class, () -> {
            patientService.findPatientByIdCard(idCard);
        });
        assertEquals(notFoundException.getMessage(), Message.patientNotFound);
    }
    
}
