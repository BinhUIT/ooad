package com.example.ooad.service.inventory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.MedicineImport;
import com.example.ooad.domain.entity.MedicineInventory;
import com.example.ooad.domain.entity.MedicinePrice;
import com.example.ooad.domain.enums.EMedicineStorageCondition;
import com.example.ooad.domain.enums.EMedicineUnit;
import com.example.ooad.dto.request.inventory.MedicineFilterRequest;
import com.example.ooad.dto.request.inventory.MedicineRequest;
import com.example.ooad.dto.response.inventory.MedicineDetailResponse;
import com.example.ooad.dto.response.inventory.MedicineResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.MedicineInventoryRepository;
import com.example.ooad.repository.MedicinePriceRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.repository.PrescriptionDetailRepository;
import com.example.ooad.service.inventory.implementation.MedicineServiceImplementation;

@ExtendWith(MockitoExtension.class)
class MedicineServiceTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineInventoryRepository medicineInventoryRepository;

    @Mock
    private MedicinePriceRepository medicinePriceRepository;

    @Mock
    private PrescriptionDetailRepository prescriptionDetailRepository;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private MedicineServiceImplementation medicineService;

    private Medicine testMedicine;
    private MedicineRequest testRequest;

    @BeforeEach
    void setUp() {
        testMedicine = new Medicine();
        testMedicine.setMedicineId(1);
        testMedicine.setMedicineName("Paracetamol");
        testMedicine.setUnit(EMedicineUnit.TABLET);
        testMedicine.setConcentration("500mg");
        testMedicine.setForm("Tablet");
        testMedicine.setManufacturer("Test Pharma");
        testMedicine.setUsageInstructions("Take 1-2 tablets every 4-6 hours");
        testMedicine.setStorageCondition(EMedicineStorageCondition.NORMAL);

        testRequest = new MedicineRequest();
        testRequest.setMedicineName("Paracetamol");
        testRequest.setUnit(EMedicineUnit.TABLET);
        testRequest.setConcentration("500mg");
        testRequest.setForm("Tablet");
        testRequest.setManufacturer("Test Pharma");
        testRequest.setUsageInstructions("Take 1-2 tablets every 4-6 hours");
        testRequest.setStorageCondition(EMedicineStorageCondition.NORMAL);
    }

    @Test
    void testGetMedicineList_Success() {
        // Arrange
        MedicineFilterRequest filter = new MedicineFilterRequest();
        filter.setPage(0);
        filter.setSize(10);
        filter.setSortBy("medicineName");
        filter.setSortType("ASC");

        List<Medicine> medicines = Arrays.asList(testMedicine);
        Page<Medicine> medicinePage = new PageImpl<>(medicines);

        when(medicineRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(medicinePage);
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(anyInt()))
                .thenReturn(100);

        // Act
        Page<MedicineResponse> result = medicineService.getMedicineList(filter);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Paracetamol", result.getContent().get(0).getMedicineName());
        verify(medicineRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testGetMedicineList_WithKeywordFilter() {
        // Arrange
        MedicineFilterRequest filter = new MedicineFilterRequest();
        filter.setKeyword("Para");

        List<Medicine> medicines = Arrays.asList(testMedicine);
        Page<Medicine> medicinePage = new PageImpl<>(medicines);

        when(medicineRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(medicinePage);
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(anyInt()))
                .thenReturn(100);

        // Act
        Page<MedicineResponse> result = medicineService.getMedicineList(filter);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetAllMedicines_Success() {
        // Arrange
        List<Medicine> medicines = Arrays.asList(testMedicine);
        when(medicineRepository.findAll()).thenReturn(medicines);
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(anyInt()))
                .thenReturn(50);

        // Act
        List<MedicineResponse> result = medicineService.getAllMedicines();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getMedicineName());
        assertEquals(50, result.get(0).getTotalQuantity());
    }

    @Test
    void testGetMedicineDetail_Success() {
        // Arrange
        int medicineId = 1;
        Date today = Date.valueOf(LocalDate.now());

        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(medicineId)).thenReturn(100);
        when(medicinePriceRepository.getCurrentPrice(medicineId, today))
                .thenReturn(new BigDecimal("5000"));
        when(medicineInventoryRepository.findByMedicineId(medicineId))
                .thenReturn(Arrays.asList());
        when(medicinePriceRepository.findByMedicineIdOrderByEffectiveDateDesc(medicineId))
                .thenReturn(Arrays.asList());

        // Act
        MedicineDetailResponse result = medicineService.getMedicineDetail(medicineId, false);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getMedicine());
        assertEquals("Paracetamol", result.getMedicine().getMedicineName());
        assertEquals(new BigDecimal("5000"), result.getCurrentPrice());
    }

    @Test
    void testGetMedicineDetail_NotFound() {
        // Arrange
        int medicineId = 999;
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            medicineService.getMedicineDetail(medicineId, false);
        });
    }

    @Test
    void testCreateMedicine_Success() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.existsByMedicineNameIgnoreCase(anyString())).thenReturn(false);
        when(medicineRepository.save(any(Medicine.class))).thenReturn(testMedicine);
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(anyInt())).thenReturn(0);

        // Act
        MedicineResponse result = medicineService.createMedicine(testRequest, bindingResult);

        // Assert
        assertNotNull(result);
        assertEquals("Paracetamol", result.getMedicineName());
        verify(medicineRepository).save(any(Medicine.class));
    }

    @Test
    void testCreateMedicine_ValidationError() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(
                new org.springframework.validation.ObjectError("medicineRequest", "Name is required")));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            medicineService.createMedicine(testRequest, bindingResult);
        });
    }

    @Test
    void testCreateMedicine_DuplicateName() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.existsByMedicineNameIgnoreCase(anyString())).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            medicineService.createMedicine(testRequest, bindingResult);
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testUpdateMedicine_Success() {
        // Arrange
        int medicineId = 1;
        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicineRepository.findAll()).thenReturn(Arrays.asList(testMedicine));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(testMedicine);
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(anyInt())).thenReturn(100);

        testRequest.setMedicineName("Paracetamol Updated");

        // Act
        MedicineResponse result = medicineService.updateMedicine(medicineId, testRequest, bindingResult);

        // Assert
        assertNotNull(result);
        verify(medicineRepository).save(any(Medicine.class));
    }

    @Test
    void testUpdateMedicine_NotFound() {
        // Arrange
        int medicineId = 999;
        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            medicineService.updateMedicine(medicineId, testRequest, bindingResult);
        });
    }

    @Test
    void testUpdateMedicine_DuplicateName() {
        // Arrange
        int medicineId = 1;
        Medicine anotherMedicine = new Medicine();
        anotherMedicine.setMedicineId(2);
        anotherMedicine.setMedicineName("Aspirin");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicineRepository.findAll()).thenReturn(Arrays.asList(testMedicine, anotherMedicine));

        testRequest.setMedicineName("Aspirin"); // Try to use existing name

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            medicineService.updateMedicine(medicineId, testRequest, bindingResult);
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testDeleteMedicine_Success() {
        // Arrange
        int medicineId = 1;
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(medicineId)).thenReturn(0);
        when(prescriptionDetailRepository.existsByMedicine_MedicineId(medicineId)).thenReturn(false);

        // Act
        medicineService.deleteMedicine(medicineId);

        // Assert
        verify(medicineRepository).delete(testMedicine);
    }

    @Test
    void testDeleteMedicine_NotFound() {
        // Arrange
        int medicineId = 999;
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            medicineService.deleteMedicine(medicineId);
        });
    }

    @Test
    void testDeleteMedicine_HasInventory() {
        // Arrange
        int medicineId = 1;
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(medicineId)).thenReturn(50);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            medicineService.deleteMedicine(medicineId);
        });
        assertTrue(exception.getMessage().contains("inventory"));
        verify(medicineRepository, never()).delete(any(Medicine.class));
    }

    @Test
    void testDeleteMedicine_UsedInPrescription() {
        // Arrange
        int medicineId = 1;
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(medicineId)).thenReturn(0);
        when(prescriptionDetailRepository.existsByMedicine_MedicineId(medicineId)).thenReturn(true);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            medicineService.deleteMedicine(medicineId);
        });
        assertTrue(exception.getMessage().contains("prescription"));
        verify(medicineRepository, never()).delete(any(Medicine.class));
    }

    @Test
    void testGetManufacturers_Success() {
        // Arrange
        List<String> manufacturers = Arrays.asList("Pharma A", "Pharma B", "Pharma C");
        when(medicineRepository.findDistinctManufacturers()).thenReturn(manufacturers);

        // Act
        List<String> result = medicineService.getManufacturers();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("Pharma A"));
    }

    @Test
    void testGetMedicineDetail_WithInventory() {
        // Arrange
        int medicineId = 1;
        Date today = Date.valueOf(LocalDate.now());

        MedicineImport medicineImport = new MedicineImport();
        medicineImport.setImportId(1);
        medicineImport.setImportDate(today);
        medicineImport.setSupplier("Test Supplier");

        MedicineInventory inventory = new MedicineInventory();
        inventory.setMedicineImport(medicineImport);
        inventory.setExpiryDate(Date.valueOf(LocalDate.now().plusYears(2)));
        inventory.setImportPrice(new BigDecimal("4000"));
        inventory.setQuantityInStock(100);

        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(medicineId)).thenReturn(100);
        when(medicinePriceRepository.getCurrentPrice(medicineId, today))
                .thenReturn(new BigDecimal("5000"));
        when(medicineInventoryRepository.findByMedicineId(medicineId))
                .thenReturn(Arrays.asList(inventory));
        when(medicinePriceRepository.findByMedicineIdOrderByEffectiveDateDesc(medicineId))
                .thenReturn(Arrays.asList());

        // Act
        MedicineDetailResponse result = medicineService.getMedicineDetail(medicineId, false);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getInventories().size());
        assertEquals(100, result.getInventories().get(0).getQuantityInStock());
    }

    @Test
    void testGetMedicineDetail_IncludeZeroQuantity() {
        // Arrange
        int medicineId = 1;
        Date today = Date.valueOf(LocalDate.now());

        MedicineImport medicineImport = new MedicineImport();
        medicineImport.setImportId(1);

        MedicineInventory zeroInventory = new MedicineInventory();
        zeroInventory.setMedicineImport(medicineImport);
        zeroInventory.setQuantityInStock(0);

        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(medicineId)).thenReturn(0);
        when(medicinePriceRepository.getCurrentPrice(medicineId, today)).thenReturn(null);
        when(medicineInventoryRepository.findByMedicineId(medicineId))
                .thenReturn(Arrays.asList(zeroInventory));
        when(medicinePriceRepository.findByMedicineIdOrderByEffectiveDateDesc(medicineId))
                .thenReturn(Arrays.asList());

        // Act - without zero quantity
        MedicineDetailResponse resultWithoutZero = medicineService.getMedicineDetail(medicineId, false);
        // Act - with zero quantity
        MedicineDetailResponse resultWithZero = medicineService.getMedicineDetail(medicineId, true);

        // Assert
        assertEquals(0, resultWithoutZero.getInventories().size());
        assertEquals(1, resultWithZero.getInventories().size());
    }
}
