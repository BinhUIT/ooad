package com.example.ooad.service.inventory.implementation;

import com.example.ooad.dto.response.inventory.MedicineImportListResponse;
import com.example.ooad.dto.response.inventory.WarehouseDashboardResponse;
import com.example.ooad.mapper.InventoryMapper;
import com.example.ooad.repository.MedicineImportRepository;
import com.example.ooad.repository.MedicineInventoryRepository;
import com.example.ooad.repository.MedicineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WarehouseDashboardServiceImplementation
 */
@ExtendWith(MockitoExtension.class)
class WarehouseDashboardServiceImplementationTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private MedicineInventoryRepository medicineInventoryRepository;

    @Mock
    private MedicineImportRepository medicineImportRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private WarehouseDashboardServiceImplementation warehouseDashboardService;

    private List<MedicineImportListResponse> mockImportResponses;

    @BeforeEach
    void setUp() {
        mockImportResponses = new ArrayList<>();

        MedicineImportListResponse response1 = new MedicineImportListResponse();
        response1.setImportId(1);
        mockImportResponses.add(response1);
    }

    @Test
    void getDashboardStats_ShouldReturnCompleteStatistics() {
        // Given
        when(medicineRepository.count()).thenReturn(150L);
        when(medicineInventoryRepository.countLowStockMedicines(anyInt())).thenReturn(5L);
        when(medicineInventoryRepository.countExpiringBatches(any(Date.class))).thenReturn(12L);
        when(medicineImportRepository.sumTotalValueByDateRange(any(Date.class), any(Date.class)))
                .thenReturn(BigDecimal.valueOf(50000000));
        when(medicineImportRepository.findTop5ByOrderByImportDateDesc()).thenReturn(new ArrayList<>());

        // When
        WarehouseDashboardResponse response = warehouseDashboardService.getDashboardStats();

        // Then
        assertNotNull(response);
        assertEquals(150L, response.getTotalMedicines());
        assertEquals(5L, response.getTotalOutOfStock());
        assertEquals(12L, response.getTotalExpiringSoon());
        assertNotNull(response.getRecentImports());

        verify(medicineRepository, times(1)).count();
        verify(medicineInventoryRepository, times(1)).countLowStockMedicines(50);
    }

    @Test
    void getDashboardStats_ShouldUseLowStockThresholdOf50() {
        // Given
        when(medicineRepository.count()).thenReturn(100L);
        when(medicineInventoryRepository.countLowStockMedicines(50)).thenReturn(8L);
        when(medicineInventoryRepository.countExpiringBatches(any(Date.class))).thenReturn(3L);
        when(medicineImportRepository.sumTotalValueByDateRange(any(Date.class), any(Date.class)))
            .thenReturn(BigDecimal.valueOf(1000000));
        when(medicineImportRepository.findTop5ByOrderByImportDateDesc()).thenReturn(new ArrayList<>());

        // When
        warehouseDashboardService.getDashboardStats();

        // Then
        verify(medicineInventoryRepository, times(1)).countLowStockMedicines(50);
    }
}





