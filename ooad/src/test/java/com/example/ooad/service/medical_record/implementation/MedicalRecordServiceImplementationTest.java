package com.example.ooad.service.medical_record.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.RefDiseaseType;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EPaymentStatus;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.dto.request.CreateMedicalRecordRequest;
import com.example.ooad.dto.request.UpdateMedicalRecordRequest;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.MedicalRecordRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.RefDiseaseTypeRepository;
import com.example.ooad.repository.ReceptionRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.service.auth.interfaces.AuthService;
import com.example.ooad.service.reception.interfaces.ReceptionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Medical Record Service Implementation Test")
public class MedicalRecordServiceImplementationTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepo;

    @Mock
    private ReceptionService receptionService;

    @Mock
    private AuthService authService;

    @Mock
    private StaffRepository staffRepo;

    @Mock
    private RefDiseaseTypeRepository refDiseaseTypeRepo;

    @Mock
    private InvoiceRepository invoiceRepo;

    @Mock
    private PatientRepository patientRepo;

    @Mock
    private ReceptionRepository receptionRepo;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MedicalRecordServiceImplementation medicalRecordService;

    private MedicalRecord medicalRecord;
    private Reception reception;
    private Staff doctor;
    private Patient patient;
    private Account account;
    private RefDiseaseType diseaseType;
    private CreateMedicalRecordRequest createRequest;
    private UpdateMedicalRecordRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Setup patient
        patient = new Patient();
        patient.setPatientId(1);
        patient.setFullName("Test Patient");
        patient.setPhone("0123456789");

        // Setup account
        account = new Account();
        account.setAccountId(1);
        account.setRole(ERole.DOCTOR);

        // Setup doctor
        doctor = new Staff();
        doctor.setStaffId(1);
        doctor.setFullName("Dr. Test");
        doctor.setAccount(account);

        // Setup disease type
        diseaseType = new RefDiseaseType();
        diseaseType.setDiseaseTypeId(1);
        diseaseType.setDiseaseName("Common Cold");
        diseaseType.setDiseaseCode("J00");

        // Setup reception
        reception = new Reception();
        reception.setReceptionId(1);
        reception.setPatient(patient);
        reception.setStatus(EReceptionStatus.WAITING);
        reception.setReceptionDate(Date.valueOf(LocalDate.now()));

        // Setup medical record
        medicalRecord = new MedicalRecord();
        medicalRecord.setRecordId(1);
        medicalRecord.setReception(reception);
        medicalRecord.setDoctor(doctor);
        medicalRecord.setExaminateDate(Date.valueOf(LocalDate.now()));
        medicalRecord.setSymptoms("Fever, cough");
        medicalRecord.setDiagnosis("Common cold");
        medicalRecord.setDiseaseType(diseaseType);
        medicalRecord.setOrderedServices("Blood test, X-ray");
        medicalRecord.setNotes("Rest and drink plenty of water");

        // Setup create request
        createRequest = new CreateMedicalRecordRequest();
        createRequest.setReceptionId(1);
        createRequest.setDiagnosis("Common cold");
        createRequest.setSymptoms("Fever, cough");
        createRequest.setDiseaseTypeId(1);
        createRequest.setOrderedServices("Blood test, X-ray");
        createRequest.setNotes("Rest and drink plenty of water");
        createRequest.setCreateInvoice(true);

        // Setup update request
        updateRequest = new UpdateMedicalRecordRequest();
        updateRequest.setDiagnosis("Severe cold");
        updateRequest.setSymptoms("High fever, persistent cough");
        updateRequest.setDiseaseTypeId(1);
        updateRequest.setOrderedServices("Blood test, X-ray, CT scan");
        updateRequest.setNotes("Prescribe antibiotics");
    }

    @Test
    @DisplayName("Should find medical record by ID successfully")
    void testFindMedicalRecordById_Success() {
        // Arrange
        when(medicalRecordRepo.findById(1)).thenReturn(Optional.of(medicalRecord));

        // Act
        MedicalRecord result = medicalRecordService.findMedicalRecordById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getRecordId());
        assertEquals("Common cold", result.getDiagnosis());
        verify(medicalRecordRepo, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should throw NotFoundException when medical record not found")
    void testFindMedicalRecordById_NotFound() {
        // Arrange
        when(medicalRecordRepo.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            medicalRecordService.findMedicalRecordById(999);
        });
        verify(medicalRecordRepo, times(1)).findById(999);
    }

    @Test
    @DisplayName("Should create medical record successfully")
    void testCreateMedicalRecord_Success() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authService.getAccountFromAuth(authentication)).thenReturn(account);
        when(staffRepo.findByAccountId(1)).thenReturn(Optional.of(doctor));
        when(receptionService.getReceptionById(1)).thenReturn(reception);
        when(refDiseaseTypeRepo.findById(1)).thenReturn(Optional.of(diseaseType));
        when(medicalRecordRepo.save(any(MedicalRecord.class))).thenReturn(medicalRecord);
        when(receptionRepo.save(any(Reception.class))).thenReturn(reception);
        when(invoiceRepo.save(any(Invoice.class))).thenReturn(new Invoice());

        // Act
        MedicalRecord result = medicalRecordService.createMedicalRecord(createRequest, bindingResult, authentication);

        // Assert
        assertNotNull(result);
        verify(medicalRecordRepo, times(1)).save(any(MedicalRecord.class));
        verify(receptionRepo, times(1)).save(any(Reception.class));
        verify(invoiceRepo, times(1)).save(any(Invoice.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when binding result has errors")
    void testCreateMedicalRecord_BindingResultHasErrors() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
                List.of(new org.springframework.validation.ObjectError("error", "Validation error")));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            medicalRecordService.createMedicalRecord(createRequest, bindingResult, authentication);
        });
    }

    @Test
    @DisplayName("Should throw BadRequestException when user is not a doctor")
    void testCreateMedicalRecord_NotDoctor() {
        // Arrange
        account.setRole(ERole.PATIENT);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authService.getAccountFromAuth(authentication)).thenReturn(account);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            medicalRecordService.createMedicalRecord(createRequest, bindingResult, authentication);
        });
    }

    @Test
    @DisplayName("Should throw BadRequestException when reception is completed")
    void testCreateMedicalRecord_ReceptionCompleted() {
        // Arrange
        reception.setStatus(EReceptionStatus.DONE);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authService.getAccountFromAuth(authentication)).thenReturn(account);
        when(staffRepo.findByAccountId(1)).thenReturn(Optional.of(doctor));
        when(receptionService.getReceptionById(1)).thenReturn(reception);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            medicalRecordService.createMedicalRecord(createRequest, bindingResult, authentication);
        });
    }

    @Test
    @DisplayName("Should update medical record successfully")
    void testUpdateMedicalRecord_Success() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authService.getAccountFromAuth(authentication)).thenReturn(account);
        when(medicalRecordRepo.findById(1)).thenReturn(Optional.of(medicalRecord));
        when(refDiseaseTypeRepo.findById(1)).thenReturn(Optional.of(diseaseType));
        when(medicalRecordRepo.save(any(MedicalRecord.class))).thenReturn(medicalRecord);

        // Act
        MedicalRecord result = medicalRecordService.updateMedicalRecord(updateRequest, bindingResult, 1,
                authentication);

        // Assert
        assertNotNull(result);
        verify(authService, times(1)).getAccountFromAuth(authentication);
        verify(medicalRecordRepo, times(1)).findById(1);
        verify(medicalRecordRepo, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    @DisplayName("Should get list of medical records for doctor")
    void testGetListMedicalRecordsForDoctor_Success() {
        // Arrange
        List<MedicalRecord> records = List.of(medicalRecord);
        Page<MedicalRecord> page = new PageImpl<>(records);

        when(medicalRecordRepo.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        Page<MedicalRecord> result = medicalRecordService.getListMedicalRecordsForDoctor(0, 10, Optional.empty(),
                Optional.empty());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(medicalRecordRepo, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should get medical records of patient")
    void testGetMedicalRecordsOfPatient_Success() {
        // Arrange
        List<MedicalRecord> records = List.of(medicalRecord);

        when(authService.getAccountFromAuth(authentication)).thenReturn(account);
        account.setRole(ERole.PATIENT);
        when(patientRepo.findByAccountId(1)).thenReturn(Optional.of(patient));
        when(receptionRepo.findByPatient_PatientId(1)).thenReturn(List.of(reception));
        when(medicalRecordRepo.findByReception_ReceptionIdIn(any(List.class))).thenReturn(records);

        // Act
        List<MedicalRecord> result = medicalRecordService.getMedicalRecordsOfPatient(authentication);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(authService, times(1)).getAccountFromAuth(authentication);
        verify(patientRepo, times(1)).findByAccountId(1);
    }

    @Test
    @DisplayName("Should throw BadRequestException when user is not a patient")
    void testGetMedicalRecordsOfPatient_NotPatient() {
        // Arrange
        account.setRole(ERole.DOCTOR);
        when(authService.getAccountFromAuth(authentication)).thenReturn(account);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            medicalRecordService.getMedicalRecordsOfPatient(authentication);
        });
    }

    @Test
    @DisplayName("Should find all medical records")
    void testFindAllRecords_Success() {
        // Arrange
        List<MedicalRecord> records = List.of(medicalRecord);
        when(medicalRecordRepo.findAllByOrderByRecordIdDesc()).thenReturn(records);

        // Act
        List<MedicalRecord> result = medicalRecordService.findAllRecords();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicalRecordRepo, times(1)).findAllByOrderByRecordIdDesc();
    }
}
