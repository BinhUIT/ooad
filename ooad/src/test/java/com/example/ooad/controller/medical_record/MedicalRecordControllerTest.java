package com.example.ooad.controller.medical_record;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.RefDiseaseType;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.dto.request.CreateMedicalRecordRequest;
import com.example.ooad.dto.request.UpdateMedicalRecordRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.MedicalRecordDetailResponse;
import com.example.ooad.service.medical_record.interfaces.MedicalRecordService;
import com.example.ooad.service.patient.interfaces.PatientService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Medical Record Controller Test")
public class MedicalRecordControllerTest {

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private PatientService patientService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    private MedicalRecord medicalRecord;
    private Reception reception;
    private Staff doctor;
    private Patient patient;
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

        // Setup doctor
        doctor = new Staff();
        doctor.setStaffId(1);
        doctor.setFullName("Dr. Test");

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
    @DisplayName("Should get list of medical records for doctor")
    void testGetListMedicalRecordsForDoctor_Success() {
        // Arrange
        List<MedicalRecord> records = List.of(medicalRecord);
        Page<MedicalRecord> page = new PageImpl<>(records);
        when(medicalRecordService.getListMedicalRecordsForDoctor(anyInt(), anyInt(), any(Optional.class),
                any(Optional.class)))
                .thenReturn(page);

        // Act
        ResponseEntity<GlobalResponse<Page<MedicalRecord>>> response = medicalRecordController
                .getListMedicalRecordsForDoctor(0, 10, Optional.empty(), Optional.empty());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().getTotalElements());
        verify(medicalRecordService, times(1)).getListMedicalRecordsForDoctor(anyInt(), anyInt(),
                any(Optional.class), any(Optional.class));
    }

    @Test
    @DisplayName("Should create medical record successfully")
    void testCreateMedicalRecord_Success() {
        // Arrange
        when(medicalRecordService.createMedicalRecord(any(CreateMedicalRecordRequest.class),
                any(BindingResult.class), any(Authentication.class)))
                .thenReturn(medicalRecord);

        // Act
        ResponseEntity<GlobalResponse<MedicalRecordDetailResponse>> response = medicalRecordController
                .createMedicalRecord(
                        createRequest, bindingResult, authentication);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(1, response.getBody().getData().getRecordId());
        verify(medicalRecordService, times(1)).createMedicalRecord(any(CreateMedicalRecordRequest.class),
                any(BindingResult.class), any(Authentication.class));
    }

    @Test
    @DisplayName("Should update medical record successfully")
    void testUpdateMedicalRecord_Success() {
        // Arrange
        when(medicalRecordService.updateMedicalRecord(any(UpdateMedicalRecordRequest.class), any(BindingResult.class),
                anyInt(),
                any(Authentication.class)))
                .thenReturn(medicalRecord);

        // Act
        ResponseEntity<GlobalResponse<MedicalRecord>> response = medicalRecordController.updateMedicalRecord(
                updateRequest, bindingResult, 1, authentication);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        verify(medicalRecordService, times(1)).updateMedicalRecord(any(UpdateMedicalRecordRequest.class),
                any(BindingResult.class), eq(1), any(Authentication.class));
    }

    @Test
    @DisplayName("Should get medical records of patient")
    void testGetMedicalRecordsOfPatient_Success() {
        // Arrange
        List<MedicalRecord> records = List.of(medicalRecord);
        when(medicalRecordService.getMedicalRecordsOfPatient(any(Authentication.class))).thenReturn(records);

        // Act
        ResponseEntity<GlobalResponse<List<MedicalRecord>>> response = medicalRecordController
                .getMedicalRecordsOfPatient(authentication);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
        verify(medicalRecordService, times(1)).getMedicalRecordsOfPatient(any(Authentication.class));
    }

    @Test
    @DisplayName("Should get medical record by ID")
    void testGetMedicalRecordById_Success() {
        // Arrange
        when(medicalRecordService.findMedicalRecordById(1)).thenReturn(medicalRecord);

        // Act
        ResponseEntity<GlobalResponse<MedicalRecord>> response = medicalRecordController.getMedicalRecordById(1);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(1, response.getBody().getData().getRecordId());
        verify(medicalRecordService, times(1)).findMedicalRecordById(1);
    }
}
