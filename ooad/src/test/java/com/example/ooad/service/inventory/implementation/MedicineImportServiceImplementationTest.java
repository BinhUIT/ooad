package com.example.ooad.service.inventory.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.ImportDetail;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.MedicineImport;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EMedicineUnit;
import com.example.ooad.dto.request.inventory.ImportDetailRequest;
import com.example.ooad.dto.request.inventory.MedicineImportFilterRequest;
import com.example.ooad.dto.request.inventory.MedicineImportRequest;
import com.example.ooad.dto.response.inventory.MedicineImportDetailResponse;
import com.example.ooad.dto.response.inventory.MedicineImportListResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.ImportDetailRepository;
import com.example.ooad.repository.MedicineImportRepository;
import com.example.ooad.repository.MedicineInventoryRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.repository.StaffRepository;

@ExtendWith(MockitoExtension.class)
public class MedicineImportServiceImplementationTest {

    @Mock
    private MedicineImportRepository medicineImportRepository;

    @Mock
    private ImportDetailRepository importDetailRepository;

    @Mock
    private MedicineInventoryRepository medicineInventoryRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private MedicineImportServiceImplementation importService;

    private MedicineImport testImport;
    private Medicine testMedicine;
    private Staff testStaff;
    private ImportDetail testDetail;
    private MedicineImportRequest validRequest;

    @BeforeEach
    void setUp() {
        // Setup test staff
        testStaff = new Staff();
        testStaff.setStaffId(1);
        testStaff.setFullName("Nguyen Van A");

        // Setup test medicine
        testMedicine = new Medicine();
        testMedicine.setMedicineId(1);
        testMedicine.setMedicineName("Paracetamol");
        testMedicine.setUnit(EMedicineUnit.TABLET);

        // Setup test import
        testImport = new MedicineImport();
        testImport.setImportId(1);
        testImport.setSupplier("NCC ABC");
        testImport.setImportDate(Date.valueOf(LocalDate.now()));
        testImport.setImporter(testStaff);
        testImport.setTotalQuantity(100);
        testImport.setTotalValue(BigDecimal.valueOf(500000));

        // Setup test detail
        testDetail = new ImportDetail();
        testDetail.setMedicineImport(testImport);
        testDetail.setMedicine(testMedicine);
        testDetail.setQuantity(100);
        testDetail.setImportPrice(5000f);
        testDetail.setExpiryDate(Date.valueOf(LocalDate.now().plusMonths(12)));

        // Setup valid request
        ImportDetailRequest detailRequest = new ImportDetailRequest();
        detailRequest.setMedicineId(1);
        detailRequest.setQuantity(100);
        detailRequest.setImportPrice(BigDecimal.valueOf(5000));
        detailRequest.setExpiryDate(Date.valueOf(LocalDate.now().plusMonths(12)));

        validRequest = new MedicineImportRequest();
        validRequest.setSupplier("NCC ABC");
        validRequest.setImportDate(Date.valueOf(LocalDate.now()));
        validRequest.setDetails(Arrays.asList(detailRequest));
    }

    // ==================== GET IMPORT LIST TESTS ====================

    @Test
    @DisplayName("Get Import List - Success: Returns paginated list")
    @SuppressWarnings("unchecked")
    void getImportList_Success() {
        MedicineImportFilterRequest filter = new MedicineImportFilterRequest();
        filter.setPage(0);
        filter.setSize(10);

        Page<MedicineImport> page = new PageImpl<>(Arrays.asList(testImport));
        when(medicineImportRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<MedicineImportListResponse> result = importService.getImportList(filter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(medicineImportRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Get Import List - Success: Filter by date range")
    @SuppressWarnings("unchecked")
    void getImportList_FilterByDateRange() {
        MedicineImportFilterRequest filter = new MedicineImportFilterRequest();
        filter.setFromDate(Date.valueOf(LocalDate.now().minusDays(7)));
        filter.setToDate(Date.valueOf(LocalDate.now()));

        Page<MedicineImport> page = new PageImpl<>(Arrays.asList(testImport));
        when(medicineImportRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<MedicineImportListResponse> result = importService.getImportList(filter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Get Import List - Success: Filter by supplier name")
    @SuppressWarnings("unchecked")
    void getImportList_FilterBySupplier() {
        MedicineImportFilterRequest filter = new MedicineImportFilterRequest();
        filter.setSupplierName("NCC");

        Page<MedicineImport> page = new PageImpl<>(Arrays.asList(testImport));
        when(medicineImportRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<MedicineImportListResponse> result = importService.getImportList(filter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Get Import List - Success: Empty List")
    @SuppressWarnings("unchecked")
    void getImportList_Empty() {
        MedicineImportFilterRequest filter = new MedicineImportFilterRequest();

        Page<MedicineImport> page = new PageImpl<>(Collections.emptyList());
        when(medicineImportRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<MedicineImportListResponse> result = importService.getImportList(filter);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    // ==================== GET ALL IMPORTS TESTS ====================

    @Test
    @DisplayName("Get All Imports - Success")
    void getAllImports_Success() {
        when(medicineImportRepository.findAll()).thenReturn(Arrays.asList(testImport));

        List<MedicineImportListResponse> result = importService.getAllImports();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(medicineImportRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get All Imports - Empty List")
    void getAllImports_Empty() {
        when(medicineImportRepository.findAll()).thenReturn(Collections.emptyList());

        List<MedicineImportListResponse> result = importService.getAllImports();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== GET IMPORT DETAIL TESTS ====================

    @Test
    @DisplayName("Get Import Detail - Success")
    void getImportDetail_Success() {
        when(medicineImportRepository.findById(1)).thenReturn(Optional.of(testImport));
        when(importDetailRepository.findByImportId(1)).thenReturn(Arrays.asList(testDetail));

        MedicineImportDetailResponse result = importService.getImportDetail(1);

        assertNotNull(result);
        assertEquals(1, result.getImportId());
        assertEquals("NCC ABC", result.getSupplier());
        assertNotNull(result.getDetails());
        assertEquals(1, result.getDetails().size());
        verify(medicineImportRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Get Import Detail - Fail: Not Found")
    void getImportDetail_NotFound() {
        when(medicineImportRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            importService.getImportDetail(999);
        });

        assertEquals("Medicine import not found with id: 999", exception.getMessage());
    }

    // ==================== CREATE IMPORT TESTS ====================

    @Test
    @DisplayName("Create Import - Success")
    void createImport_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(staffRepository.findById(1)).thenReturn(Optional.of(testStaff));
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineImportRepository.save(any(MedicineImport.class))).thenReturn(testImport);
        when(importDetailRepository.save(any(ImportDetail.class))).thenReturn(testDetail);
        when(importDetailRepository.findByImportId(anyInt())).thenReturn(Arrays.asList(testDetail));

        MedicineImportDetailResponse result = importService.createImport(validRequest, bindingResult, 1);

        assertNotNull(result);
        verify(medicineImportRepository, times(1)).save(any(MedicineImport.class));
        verify(importDetailRepository, times(1)).save(any(ImportDetail.class));
        verify(medicineInventoryRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Create Import - Fail: Validation Error")
    void createImport_ValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
            Arrays.asList(new org.springframework.validation.ObjectError("supplier", "Supplier is required"))
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            importService.createImport(validRequest, bindingResult, 1);
        });

        assertEquals("Supplier is required", exception.getMessage());
        verify(medicineImportRepository, never()).save(any(MedicineImport.class));
    }

    @Test
    @DisplayName("Create Import - Fail: Empty Details")
    void createImport_EmptyDetails() {
        validRequest.setDetails(Collections.emptyList());
        when(bindingResult.hasErrors()).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            importService.createImport(validRequest, bindingResult, 1);
        });

        assertEquals("Import must have at least one detail", exception.getMessage());
        verify(medicineImportRepository, never()).save(any(MedicineImport.class));
    }

    @Test
    @DisplayName("Create Import - Fail: Null Details")
    void createImport_NullDetails() {
        validRequest.setDetails(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            importService.createImport(validRequest, bindingResult, 1);
        });

        assertEquals("Import must have at least one detail", exception.getMessage());
    }

    @Test
    @DisplayName("Create Import - Fail: Staff Not Found")
    void createImport_StaffNotFound() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(staffRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            importService.createImport(validRequest, bindingResult, 999);
        });

        assertEquals("Staff not found with id: 999", exception.getMessage());
        verify(medicineImportRepository, never()).save(any(MedicineImport.class));
    }

    @Test
    @DisplayName("Create Import - Fail: Medicine Not Found")
    void createImport_MedicineNotFound() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(staffRepository.findById(1)).thenReturn(Optional.of(testStaff));
        when(medicineImportRepository.save(any(MedicineImport.class))).thenReturn(testImport);
        when(medicineRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            importService.createImport(validRequest, bindingResult, 1);
        });

        assertEquals("Medicine not found with id: 1", exception.getMessage());
    }

    // ==================== UPDATE IMPORT TESTS ====================

    @Test
    @DisplayName("Update Import - Success")
    void updateImport_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineImportRepository.findById(1)).thenReturn(Optional.of(testImport));
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineImportRepository.save(any(MedicineImport.class))).thenReturn(testImport);
        when(importDetailRepository.save(any(ImportDetail.class))).thenReturn(testDetail);
        when(importDetailRepository.findByImportId(1)).thenReturn(Arrays.asList(testDetail));

        MedicineImportDetailResponse result = importService.updateImport(1, validRequest, bindingResult);

        assertNotNull(result);
        verify(importDetailRepository, times(1)).deleteByImportId(1);
        verify(medicineInventoryRepository, times(1)).deleteByImportId(1);
        verify(medicineImportRepository, times(1)).save(any(MedicineImport.class));
    }

    @Test
    @DisplayName("Update Import - Fail: Not Found")
    void updateImport_NotFound() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineImportRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            importService.updateImport(999, validRequest, bindingResult);
        });

        assertEquals("Medicine import not found with id: 999", exception.getMessage());
        verify(medicineImportRepository, never()).save(any(MedicineImport.class));
    }

    @Test
    @DisplayName("Update Import - Fail: Validation Error")
    void updateImport_ValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
            Arrays.asList(new org.springframework.validation.ObjectError("supplier", "Supplier is required"))
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            importService.updateImport(1, validRequest, bindingResult);
        });

        assertEquals("Supplier is required", exception.getMessage());
        verify(medicineImportRepository, never()).findById(anyInt());
    }

    // ==================== DELETE IMPORT TESTS ====================

    @Test
    @DisplayName("Delete Import - Success")
    void deleteImport_Success() {
        when(medicineImportRepository.existsById(1)).thenReturn(true);

        importService.deleteImport(1);

        verify(medicineInventoryRepository, times(1)).deleteByImportId(1);
        verify(importDetailRepository, times(1)).deleteByImportId(1);
        verify(medicineImportRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Delete Import - Fail: Not Found")
    void deleteImport_NotFound() {
        when(medicineImportRepository.existsById(999)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            importService.deleteImport(999);
        });

        assertEquals("Medicine import not found with id: 999", exception.getMessage());
        verify(medicineImportRepository, never()).deleteById(anyInt());
        verify(importDetailRepository, never()).deleteByImportId(anyInt());
        verify(medicineInventoryRepository, never()).deleteByImportId(anyInt());
    }

    // ==================== CALCULATE TOTALS TESTS ====================

    @Test
    @DisplayName("Create Import - Correct Total Calculation")
    void createImport_CorrectTotalCalculation() {
        // Setup: 2 details with different quantities and prices
        ImportDetailRequest detail1 = new ImportDetailRequest();
        detail1.setMedicineId(1);
        detail1.setQuantity(100);
        detail1.setImportPrice(BigDecimal.valueOf(5000));
        detail1.setExpiryDate(Date.valueOf(LocalDate.now().plusMonths(12)));

        ImportDetailRequest detail2 = new ImportDetailRequest();
        detail2.setMedicineId(1);
        detail2.setQuantity(50);
        detail2.setImportPrice(BigDecimal.valueOf(6000));
        detail2.setExpiryDate(Date.valueOf(LocalDate.now().plusMonths(6)));

        MedicineImportRequest request = new MedicineImportRequest();
        request.setSupplier("NCC ABC");
        request.setImportDate(Date.valueOf(LocalDate.now()));
        request.setDetails(Arrays.asList(detail1, detail2));

        // Total quantity = 100 + 50 = 150
        // Total value = (100 * 5000) + (50 * 6000) = 500000 + 300000 = 800000
        testImport.setTotalQuantity(150);
        testImport.setTotalValue(BigDecimal.valueOf(800000));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(staffRepository.findById(1)).thenReturn(Optional.of(testStaff));
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineImportRepository.save(any(MedicineImport.class))).thenReturn(testImport);
        when(importDetailRepository.save(any(ImportDetail.class))).thenReturn(testDetail);
        when(importDetailRepository.findByImportId(anyInt())).thenReturn(Arrays.asList(testDetail));

        MedicineImportDetailResponse result = importService.createImport(request, bindingResult, 1);

        assertNotNull(result);
        assertEquals(150, result.getTotalQuantity());
        assertEquals(BigDecimal.valueOf(800000), result.getTotalValue());
    }
}
