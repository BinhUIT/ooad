package com.example.ooad.service.inventory.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.example.ooad.domain.compositekey.MedicineInventoryKey;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.MedicineImport;
import com.example.ooad.domain.entity.MedicineInventory;
import com.example.ooad.domain.enums.EMedicineStorageCondition;
import com.example.ooad.domain.enums.EMedicineUnit;
import com.example.ooad.dto.request.inventory.InventoryFilterRequest;
import com.example.ooad.dto.response.inventory.InventoryDetailResponse;
import com.example.ooad.dto.response.inventory.InventoryItemResponse;
import com.example.ooad.dto.response.inventory.MedicineSelectionResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.MedicineImportRepository;
import com.example.ooad.repository.MedicineInventoryRepository;
import com.example.ooad.repository.MedicineRepository;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplementationTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineInventoryRepository medicineInventoryRepository;

    @Mock
    private MedicineImportRepository medicineImportRepository;

    @InjectMocks
    private InventoryServiceImplementation inventoryService;

    private Medicine testMedicine;
    private Medicine testMedicine2;
    private MedicineImport testImport;
    private MedicineInventory testInventory;
    private MedicineInventory testInventory2;

    @BeforeEach
    void setUp() {
        // Setup test medicine
        testMedicine = new Medicine();
        testMedicine.setMedicineId(1);
        testMedicine.setMedicineName("Paracetamol");
        testMedicine.setUnit(EMedicineUnit.TABLET);
        testMedicine.setConcentration("500mg");
        testMedicine.setManufacturer("ABC Pharma");
        testMedicine.setStorageCondition(EMedicineStorageCondition.NORMAL);

        testMedicine2 = new Medicine();
        testMedicine2.setMedicineId(2);
        testMedicine2.setMedicineName("Amoxicillin");
        testMedicine2.setUnit(EMedicineUnit.BLISTER);
        testMedicine2.setConcentration("250mg");
        testMedicine2.setManufacturer("XYZ Pharma");

        // Setup test import
        testImport = new MedicineImport();
        testImport.setImportId(1);
        testImport.setSupplier("NCC ABC");
        testImport.setImportDate(Date.valueOf(LocalDate.now().minusDays(30)));

        // Setup test inventory
        MedicineInventoryKey key1 = new MedicineInventoryKey(1, 1);
        testInventory = new MedicineInventory();
        testInventory.setMedicineInventoryId(key1);
        testInventory.setMedicine(testMedicine);
        testInventory.setMedicineImport(testImport);
        testInventory.setQuantityInStock(100);
        testInventory.setExpiryDate(Date.valueOf(LocalDate.now().plusMonths(6)));
        testInventory.setImportPrice(BigDecimal.valueOf(5000));

        MedicineInventoryKey key2 = new MedicineInventoryKey(1, 2);
        testInventory2 = new MedicineInventory();
        testInventory2.setMedicineInventoryId(key2);
        testInventory2.setMedicine(testMedicine);
        testInventory2.setMedicineImport(testImport);
        testInventory2.setQuantityInStock(50);
        testInventory2.setExpiryDate(Date.valueOf(LocalDate.now().plusMonths(12)));
        testInventory2.setImportPrice(BigDecimal.valueOf(5500));
    }

    // ==================== GET INVENTORY LIST TESTS ====================

    @Test
    @DisplayName("Get Inventory List - Success: Returns paginated list")
    void getInventoryList_Success() {
        InventoryFilterRequest filter = new InventoryFilterRequest();
        filter.setPage(0);
        filter.setSize(10);
        filter.setIncludeOutOfStock(true);

        when(medicineRepository.findAll()).thenReturn(Arrays.asList(testMedicine, testMedicine2));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(1)).thenReturn(150);
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(2)).thenReturn(0);
        when(medicineInventoryRepository.getNearestExpiryDateByMedicineId(1))
            .thenReturn(Date.valueOf(LocalDate.now().plusMonths(6)));
        when(medicineInventoryRepository.getNearestExpiryDateByMedicineId(2)).thenReturn(null);

        Page<InventoryItemResponse> result = inventoryService.getInventoryList(filter);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Paracetamol", result.getContent().get(0).getMedicineName());
        verify(medicineRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get Inventory List - Success: Filter out of stock")
    void getInventoryList_FilterOutOfStock() {
        InventoryFilterRequest filter = new InventoryFilterRequest();
        filter.setIncludeOutOfStock(false);

        when(medicineRepository.findAll()).thenReturn(Arrays.asList(testMedicine, testMedicine2));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(1)).thenReturn(150);
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(2)).thenReturn(0);
        when(medicineInventoryRepository.getNearestExpiryDateByMedicineId(anyInt())).thenReturn(null);

        Page<InventoryItemResponse> result = inventoryService.getInventoryList(filter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Paracetamol", result.getContent().get(0).getMedicineName());
    }

    @Test
    @DisplayName("Get Inventory List - Success: Search by keyword")
    void getInventoryList_SearchByKeyword() {
        InventoryFilterRequest filter = new InventoryFilterRequest();
        filter.setKeyword("para");
        filter.setIncludeOutOfStock(true);

        when(medicineRepository.findAll()).thenReturn(Arrays.asList(testMedicine, testMedicine2));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(1)).thenReturn(150);
        when(medicineInventoryRepository.getNearestExpiryDateByMedicineId(1))
            .thenReturn(Date.valueOf(LocalDate.now().plusMonths(6)));

        Page<InventoryItemResponse> result = inventoryService.getInventoryList(filter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Paracetamol", result.getContent().get(0).getMedicineName());
    }

    @Test
    @DisplayName("Get Inventory List - Success: Sort by total quantity DESC")
    void getInventoryList_SortByQuantityDesc() {
        InventoryFilterRequest filter = new InventoryFilterRequest();
        filter.setSortBy("totalQuantity");
        filter.setSortType("DESC");
        filter.setIncludeOutOfStock(true);

        when(medicineRepository.findAll()).thenReturn(Arrays.asList(testMedicine, testMedicine2));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(1)).thenReturn(50);
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(2)).thenReturn(200);
        when(medicineInventoryRepository.getNearestExpiryDateByMedicineId(anyInt())).thenReturn(null);

        Page<InventoryItemResponse> result = inventoryService.getInventoryList(filter);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        // DESC order: Amoxicillin (200) should come first
        assertEquals("Amoxicillin", result.getContent().get(0).getMedicineName());
        assertEquals("Paracetamol", result.getContent().get(1).getMedicineName());
    }

    // ==================== GET ALL INVENTORY ITEMS TESTS ====================

    @Test
    @DisplayName("Get All Inventory Items - Success")
    void getAllInventoryItems_Success() {
        when(medicineRepository.findAll()).thenReturn(Arrays.asList(testMedicine, testMedicine2));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(anyInt())).thenReturn(100);
        when(medicineInventoryRepository.getNearestExpiryDateByMedicineId(anyInt())).thenReturn(null);

        List<InventoryItemResponse> result = inventoryService.getAllInventoryItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(medicineRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get All Inventory Items - Empty List")
    void getAllInventoryItems_Empty() {
        when(medicineRepository.findAll()).thenReturn(Collections.emptyList());

        List<InventoryItemResponse> result = inventoryService.getAllInventoryItems();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== GET INVENTORY DETAIL TESTS ====================

    @Test
    @DisplayName("Get Inventory Detail - Success")
    void getInventoryDetail_Success() {
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.getTotalQuantityByMedicineId(1)).thenReturn(150);
        when(medicineInventoryRepository.getNearestExpiryDateByMedicineId(1))
            .thenReturn(Date.valueOf(LocalDate.now().plusMonths(6)));
        when(medicineInventoryRepository.findByMedicineId(1))
            .thenReturn(Arrays.asList(testInventory, testInventory2));

        InventoryDetailResponse result = inventoryService.getInventoryDetail(1);

        assertNotNull(result);
        assertEquals(1, result.getMedicineId());
        assertEquals("Paracetamol", result.getMedicineName());
        assertEquals(150, result.getTotalQuantity());
        assertNotNull(result.getBatches());
        assertEquals(2, result.getBatches().size());
    }

    @Test
    @DisplayName("Get Inventory Detail - Fail: Medicine Not Found")
    void getInventoryDetail_NotFound() {
        when(medicineRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            inventoryService.getInventoryDetail(999);
        });

        assertEquals("Medicine not found with id: 999", exception.getMessage());
    }

    // ==================== GET MEDICINES FOR SELECTION TESTS ====================

    @Test
    @DisplayName("Get Medicines For Selection - Success")
    void getMedicinesForSelection_Success() {
        when(medicineRepository.findAll()).thenReturn(Arrays.asList(testMedicine, testMedicine2));

        List<MedicineSelectionResponse> result = inventoryService.getMedicinesForSelection();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Paracetamol", result.get(0).getMedicineName());
        assertEquals("Amoxicillin", result.get(1).getMedicineName());
    }

    // ==================== GET SUPPLIERS TESTS ====================

    @Test
    @DisplayName("Get Suppliers - Success")
    void getSuppliers_Success() {
        when(medicineImportRepository.findDistinctSuppliers())
            .thenReturn(Arrays.asList("NCC ABC", "NCC XYZ"));

        List<String> result = inventoryService.getSuppliers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("NCC ABC"));
        assertTrue(result.contains("NCC XYZ"));
    }

    // ==================== GET AVAILABLE QUANTITY TESTS ====================

    @Test
    @DisplayName("Get Available Quantity - Success")
    void getAvailableQuantity_Success() {
        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(1), any(Date.class))).thenReturn(150);

        int result = inventoryService.getAvailableQuantity(1, 3);

        assertEquals(150, result);
        verify(medicineInventoryRepository, times(1)).getTotalAvailableQuantity(eq(1), any(Date.class));
    }

    @Test
    @DisplayName("Get Available Quantity - Returns Zero When No Stock")
    void getAvailableQuantity_NoStock() {
        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(1), any(Date.class))).thenReturn(0);

        int result = inventoryService.getAvailableQuantity(1, 3);

        assertEquals(0, result);
    }

    // ==================== CHECK AVAILABILITY TESTS ====================

    @Test
    @DisplayName("Check Availability - Success: Sufficient Stock")
    void checkAvailability_SufficientStock() {
        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(1), any(Date.class))).thenReturn(150);

        boolean result = inventoryService.checkAvailability(1, 100, 3);

        assertTrue(result);
    }

    @Test
    @DisplayName("Check Availability - Fail: Insufficient Stock")
    void checkAvailability_InsufficientStock() {
        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(1), any(Date.class))).thenReturn(50);

        boolean result = inventoryService.checkAvailability(1, 100, 3);

        assertFalse(result);
    }

    @Test
    @DisplayName("Check Availability - Success: Exact Amount")
    void checkAvailability_ExactAmount() {
        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(1), any(Date.class))).thenReturn(100);

        boolean result = inventoryService.checkAvailability(1, 100, 3);

        assertTrue(result);
    }

    // ==================== CHECK BULK AVAILABILITY TESTS ====================

    @Test
    @DisplayName("Check Bulk Availability - Success: All Available")
    void checkBulkAvailability_AllAvailable() {
        Map<Integer, Integer> requirements = new HashMap<>();
        requirements.put(1, 50);
        requirements.put(2, 30);

        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(1), any(Date.class))).thenReturn(100);
        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(2), any(Date.class))).thenReturn(50);

        Map<Integer, Boolean> result = inventoryService.checkBulkAvailability(requirements, 3);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(1));
        assertTrue(result.get(2));
    }

    @Test
    @DisplayName("Check Bulk Availability - Partial: Some Not Available")
    void checkBulkAvailability_SomeNotAvailable() {
        Map<Integer, Integer> requirements = new HashMap<>();
        requirements.put(1, 50);
        requirements.put(2, 100);

        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(1), any(Date.class))).thenReturn(100);
        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(2), any(Date.class))).thenReturn(50);

        Map<Integer, Boolean> result = inventoryService.checkBulkAvailability(requirements, 3);

        assertNotNull(result);
        assertTrue(result.get(1));
        assertFalse(result.get(2));
    }

    // ==================== DEDUCT INVENTORY TESTS ====================

    @Test
    @DisplayName("Deduct Inventory - Success: Single Batch")
    void deductInventory_SingleBatch_Success() {
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.findAvailableBatchesFEFO(eq(1), any(Date.class)))
            .thenReturn(Arrays.asList(testInventory));
        when(medicineInventoryRepository.save(any(MedicineInventory.class))).thenReturn(testInventory);

        inventoryService.deductInventory(1, 50, 3);

        verify(medicineInventoryRepository, times(1)).save(any(MedicineInventory.class));
        assertEquals(50, testInventory.getQuantityInStock()); // 100 - 50 = 50
    }

    @Test
    @DisplayName("Deduct Inventory - Success: Multiple Batches (FEFO)")
    void deductInventory_MultipleBatches_Success() {
        // Setup: First batch has 30, second has 50
        testInventory.setQuantityInStock(30);
        testInventory2.setQuantityInStock(50);

        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.findAvailableBatchesFEFO(eq(1), any(Date.class)))
            .thenReturn(Arrays.asList(testInventory, testInventory2));
        when(medicineInventoryRepository.save(any(MedicineInventory.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        inventoryService.deductInventory(1, 50, 3);

        // First batch should be depleted (30 - 30 = 0)
        // Second batch should have 20 remaining (50 - 20 = 30)
        verify(medicineInventoryRepository, times(2)).save(any(MedicineInventory.class));
    }

    @Test
    @DisplayName("Deduct Inventory - Fail: Medicine Not Found")
    void deductInventory_MedicineNotFound() {
        when(medicineRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            inventoryService.deductInventory(999, 50, 3);
        });

        assertEquals("Medicine not found with id: 999", exception.getMessage());
        verify(medicineInventoryRepository, never()).save(any(MedicineInventory.class));
    }

    @Test
    @DisplayName("Deduct Inventory - Fail: Insufficient Stock")
    void deductInventory_InsufficientStock() {
        testInventory.setQuantityInStock(30);

        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.findAvailableBatchesFEFO(eq(1), any(Date.class)))
            .thenReturn(Arrays.asList(testInventory));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            inventoryService.deductInventory(1, 100, 3);
        });

        assertTrue(exception.getMessage().contains("Insufficient inventory"));
        verify(medicineInventoryRepository, never()).save(any(MedicineInventory.class));
    }

    @Test
    @DisplayName("Deduct Inventory - Fail: Zero Quantity")
    void deductInventory_ZeroQuantity() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            inventoryService.deductInventory(1, 0, 3);
        });

        assertEquals("Quantity to deduct must be positive", exception.getMessage());
        verify(medicineRepository, never()).findById(anyInt());
    }

    @Test
    @DisplayName("Deduct Inventory - Fail: Negative Quantity")
    void deductInventory_NegativeQuantity() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            inventoryService.deductInventory(1, -10, 3);
        });

        assertEquals("Quantity to deduct must be positive", exception.getMessage());
    }

    // ==================== DEDUCT BULK INVENTORY TESTS ====================

    @Test
    @DisplayName("Deduct Bulk Inventory - Success: All Deductions")
    void deductBulkInventory_Success() {
        Map<Integer, Integer> deductions = new HashMap<>();
        deductions.put(1, 30);

        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(1), any(Date.class))).thenReturn(100);
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.findAvailableBatchesFEFO(eq(1), any(Date.class)))
            .thenReturn(Arrays.asList(testInventory));
        when(medicineInventoryRepository.save(any(MedicineInventory.class))).thenReturn(testInventory);

        inventoryService.deductBulkInventory(deductions, 3);

        verify(medicineInventoryRepository, times(1)).save(any(MedicineInventory.class));
    }

    @Test
    @DisplayName("Deduct Bulk Inventory - Fail: One Medicine Insufficient")
    void deductBulkInventory_OneInsufficientStock() {
        Map<Integer, Integer> deductions = new HashMap<>();
        deductions.put(1, 30);
        deductions.put(2, 200);

        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(1), any(Date.class))).thenReturn(100);
        when(medicineInventoryRepository.getTotalAvailableQuantity(eq(2), any(Date.class))).thenReturn(50);
        when(medicineRepository.findById(2)).thenReturn(Optional.of(testMedicine2));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            inventoryService.deductBulkInventory(deductions, 3);
        });

        assertTrue(exception.getMessage().contains("Insufficient inventory"));
        // Verify no saves happened (all-or-nothing)
        verify(medicineInventoryRepository, never()).save(any(MedicineInventory.class));
    }

    // ==================== RESTORE INVENTORY TESTS ====================

    @Test
    @DisplayName("Restore Inventory - Success")
    void restoreInventory_Success() {
        testInventory.setQuantityInStock(50);

        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.findMostRecentBatch(1))
            .thenReturn(Arrays.asList(testInventory));
        when(medicineInventoryRepository.save(any(MedicineInventory.class))).thenReturn(testInventory);

        inventoryService.restoreInventory(1, 30);

        verify(medicineInventoryRepository, times(1)).save(any(MedicineInventory.class));
        assertEquals(80, testInventory.getQuantityInStock()); // 50 + 30 = 80
    }

    @Test
    @DisplayName("Restore Inventory - Fail: Medicine Not Found")
    void restoreInventory_MedicineNotFound() {
        when(medicineRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            inventoryService.restoreInventory(999, 30);
        });

        assertEquals("Medicine not found with id: 999", exception.getMessage());
        verify(medicineInventoryRepository, never()).save(any(MedicineInventory.class));
    }

    @Test
    @DisplayName("Restore Inventory - Fail: No Batch Found")
    void restoreInventory_NoBatchFound() {
        when(medicineRepository.findById(1)).thenReturn(Optional.of(testMedicine));
        when(medicineInventoryRepository.findMostRecentBatch(1))
            .thenReturn(Collections.emptyList());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            inventoryService.restoreInventory(1, 30);
        });

        assertEquals("No inventory batch found for medicine id: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Restore Inventory - Fail: Zero Quantity")
    void restoreInventory_ZeroQuantity() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            inventoryService.restoreInventory(1, 0);
        });

        assertEquals("Quantity to restore must be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Restore Inventory - Fail: Negative Quantity")
    void restoreInventory_NegativeQuantity() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            inventoryService.restoreInventory(1, -10);
        });

        assertEquals("Quantity to restore must be positive", exception.getMessage());
    }
}
