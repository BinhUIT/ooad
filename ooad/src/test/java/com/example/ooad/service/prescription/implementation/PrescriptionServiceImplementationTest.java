package com.example.ooad.service.prescription.implementation;

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
import static org.mockito.ArgumentMatchers.anyList;
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

import com.example.ooad.domain.entity.MedicalRecord;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Prescription;
import com.example.ooad.domain.entity.PrescriptionDetail;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EMedicineUnit;
import com.example.ooad.dto.request.PrescriptionDetailRequest;
import com.example.ooad.dto.request.PrescriptionRequest;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.repository.PrescriptionDetailRepository;
import com.example.ooad.repository.PrescriptionRepository;
import com.example.ooad.service.medical_record.interfaces.MedicalRecordService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Prescription Service Implementation Test")
public class PrescriptionServiceImplementationTest {

    @Mock
    private PrescriptionRepository prescriptionRepo;

    @Mock
    private PrescriptionDetailRepository prescriptionDetailRepo;

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private MedicineRepository medicineRepo;

    @InjectMocks
    private PrescriptionServiceImplementation prescriptionService;

    private Prescription prescription;
    private MedicalRecord medicalRecord;
    private Reception reception;
    private Staff doctor;
    private Patient patient;
    private Medicine medicine;
    private PrescriptionDetail prescriptionDetail;
    private PrescriptionRequest prescriptionRequest;

    @BeforeEach
    void setUp() {
        // Setup patient
        patient = new Patient();
        patient.setPatientId(1);
        patient.setFullName("Test Patient");

        // Setup doctor
        doctor = new Staff();
        doctor.setStaffId(1);
        doctor.setFullName("Dr. Test");

        // Setup reception
        reception = new Reception();
        reception.setReceptionId(1);
        reception.setPatient(patient);
        reception.setReceptionDate(Date.valueOf(LocalDate.now()));

        // Setup medical record
        medicalRecord = new MedicalRecord();
        medicalRecord.setRecordId(1);
        medicalRecord.setReception(reception);
        medicalRecord.setDoctor(doctor);
        medicalRecord.setDiagnosis("Common cold");

        // Setup prescription
        prescription = new Prescription();
        prescription.setPrescriptionId(1);
        prescription.setRecord(medicalRecord);
        prescription.setPrescriptionDate(Date.valueOf(LocalDate.now()));
        prescription.setNotes("Take medicine after meals");

        // Setup medicine
        medicine = new Medicine();
        medicine.setMedicineId(1);
        medicine.setMedicineName("Paracetamol");
        medicine.setUnit(EMedicineUnit.TABLET);

        // Setup prescription detail
        prescriptionDetail = new PrescriptionDetail();
        prescriptionDetail.setPrescription(prescription);
        prescriptionDetail.setMedicine(medicine);
        prescriptionDetail.setQuantity(10);
        prescriptionDetail.setDosage("1 tablet");
        prescriptionDetail.setDays(5);

        // Setup prescription request
        PrescriptionDetailRequest detailRequest = new PrescriptionDetailRequest();
        detailRequest.setMedicineId(1);
        detailRequest.setQuantity(10);
        detailRequest.setDosage("1 tablet");
        detailRequest.setDays(5);

        prescriptionRequest = new PrescriptionRequest();
        prescriptionRequest.setRecordId(1);
        prescriptionRequest.setNotes("Take medicine after meals");
        prescriptionRequest.setPrescriptionDetails(List.of(detailRequest));
    }

    @Test
    @DisplayName("Should get all prescriptions with pagination")
    void testGetAllPrescription_Success() {
        // Arrange
        List<Prescription> prescriptions = List.of(prescription);
        Page<Prescription> page = new PageImpl<>(prescriptions);

        when(prescriptionRepo.findPrescriptions(any(Pageable.class), any(), any())).thenReturn(page);

        // Act
        Page<Prescription> result = prescriptionService.getAllPrescription(0, 10, Optional.empty(), Optional.empty());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(prescriptionRepo, times(1)).findPrescriptions(any(Pageable.class), any(), any());
    }

    @Test
    @DisplayName("Should get all prescriptions with date filter")
    void testGetAllPrescription_WithDateFilter() {
        // Arrange
        List<Prescription> prescriptions = List.of(prescription);
        Page<Prescription> page = new PageImpl<>(prescriptions);
        Date filterDate = Date.valueOf(LocalDate.now());

        when(prescriptionRepo.findPrescriptions(any(Pageable.class), any(Date.class), any())).thenReturn(page);

        // Act
        Page<Prescription> result = prescriptionService.getAllPrescription(0, 10, Optional.of(filterDate),
                Optional.empty());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(prescriptionRepo, times(1)).findPrescriptions(any(Pageable.class), any(Date.class), any());
    }

    @Test
    @DisplayName("Should get prescription by ID successfully")
    void testGetPrescriptionById_Success() {
        // Arrange
        when(prescriptionRepo.findById(1)).thenReturn(Optional.of(prescription));

        // Act
        Prescription result = prescriptionService.getPrescriptionById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPrescriptionId());
        verify(prescriptionRepo, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should throw NotFoundException when prescription not found")
    void testGetPrescriptionById_NotFound() {
        // Arrange
        when(prescriptionRepo.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            prescriptionService.getPrescriptionById(999);
        });
        verify(prescriptionRepo, times(1)).findById(999);
    }

    @Test
    @DisplayName("Should get prescription by record ID")
    void testGetPrescriptionByRecordId_Success() {
        // Arrange
        when(prescriptionRepo.findLatestByRecordId(1)).thenReturn(Optional.of(prescription));

        // Act
        Prescription result = prescriptionService.getPrescriptionByRecordId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPrescriptionId());
        verify(prescriptionRepo, times(1)).findLatestByRecordId(1);
    }

    @Test
    @DisplayName("Should return null when no prescription found for record")
    void testGetPrescriptionByRecordId_NotFound() {
        // Arrange
        when(prescriptionRepo.findLatestByRecordId(999)).thenReturn(Optional.empty());

        // Act
        Prescription result = prescriptionService.getPrescriptionByRecordId(999);

        // Assert
        assertEquals(null, result);
        verify(prescriptionRepo, times(1)).findLatestByRecordId(999);
    }

    @Test
    @DisplayName("Should get prescription details as list")
    void testGetPrescriptionDetailOfPrescription_List() {
        // Arrange
        List<PrescriptionDetail> details = List.of(prescriptionDetail);
        when(prescriptionDetailRepo.findByPrescription_PrescriptionId(1)).thenReturn(details);

        // Act
        List<PrescriptionDetail> result = prescriptionService.getPrescriptionDetailOfPrescription(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(prescriptionDetailRepo, times(1)).findByPrescription_PrescriptionId(1);
    }

    @Test
    @DisplayName("Should get prescription details as page")
    void testGetPrescriptionDetailOfPrescription_Page() {
        // Arrange
        List<PrescriptionDetail> details = List.of(prescriptionDetail);
        Page<PrescriptionDetail> page = new PageImpl<>(details);
        when(prescriptionDetailRepo.findByPrescription_PrescriptionId(any(Pageable.class), anyInt())).thenReturn(page);

        // Act
        Page<PrescriptionDetail> result = prescriptionService.getPrescriptionDetailOfPrescription(0, 10, 1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(prescriptionDetailRepo, times(1)).findByPrescription_PrescriptionId(any(Pageable.class), anyInt());
    }

    @Test
    @DisplayName("Should create prescription successfully")
    void testCreatePrescription_Success() {
        // Arrange
        when(medicalRecordService.findMedicalRecordById(1)).thenReturn(medicalRecord);
        when(prescriptionRepo.save(any(Prescription.class))).thenReturn(prescription);
        when(medicineRepo.findByMedicineId_In(anyList())).thenReturn(List.of(medicine));
        when(prescriptionDetailRepo.saveAll(anyList())).thenReturn(List.of(prescriptionDetail));

        // Act
        Prescription result = prescriptionService.createPrescription(prescriptionRequest);

        // Assert
        assertNotNull(result);
        verify(medicalRecordService, times(1)).findMedicalRecordById(1);
        verify(prescriptionRepo, times(1)).save(any(Prescription.class));
        verify(prescriptionDetailRepo, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should update prescription successfully")
    void testUpdatePrescription_Success() {
        // Arrange
        PrescriptionDetailRequest detailRequest = new PrescriptionDetailRequest();
        detailRequest.setPrescriptionId(1); // Must include prescriptionId for update
        detailRequest.setMedicineId(1);
        detailRequest.setQuantity(10);
        detailRequest.setDosage("1 tablet");
        detailRequest.setDays(5);

        PrescriptionRequest updateRequest = new PrescriptionRequest();
        updateRequest.setRecordId(1);
        updateRequest.setNotes("Updated notes");
        updateRequest.setPrescriptionDetails(List.of(detailRequest));

        when(prescriptionRepo.findById(1)).thenReturn(Optional.of(prescription));
        when(medicalRecordService.findMedicalRecordById(1)).thenReturn(medicalRecord);
        when(prescriptionRepo.save(any(Prescription.class))).thenReturn(prescription);
        when(medicineRepo.findByMedicineId_In(anyList())).thenReturn(List.of(medicine));
        when(prescriptionDetailRepo.findByPrescription_PrescriptionId(anyInt())).thenReturn(new ArrayList<>());
        when(prescriptionDetailRepo.saveAll(anyList())).thenReturn(List.of(prescriptionDetail));

        // Act
        Prescription result = prescriptionService.updatePrescription(updateRequest, 1);

        // Assert
        assertNotNull(result);
        verify(prescriptionRepo, times(1)).findById(1);
        verify(prescriptionRepo, times(1)).save(any(Prescription.class));
        verify(prescriptionDetailRepo, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should get all medical records")
    void testGetRecords_Success() {
        // Arrange
        List<MedicalRecord> records = List.of(medicalRecord);
        when(medicalRecordService.findAllRecords()).thenReturn(records);

        // Act
        List<MedicalRecord> result = prescriptionService.getRecords();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicalRecordService, times(1)).findAllRecords();
    }

    @Test
    @DisplayName("Should get all medicines")
    void testGetMedicines_Success() {
        // Arrange
        List<Medicine> medicines = List.of(medicine);
        when(medicineRepo.findAll()).thenReturn(medicines);

        // Act
        List<Medicine> result = prescriptionService.getMedicines();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicineRepo, times(1)).findAll();
    }
}
