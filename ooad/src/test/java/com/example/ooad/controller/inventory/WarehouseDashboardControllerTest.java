package com.example.ooad.controller.inventory;

import com.example.ooad.dto.response.inventory.WarehouseDashboardResponse;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.inventory.interfaces.WarehouseDashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WarehouseDashboardController
 */
@ExtendWith(MockitoExtension.class)
class WarehouseDashboardControllerTest {

    @Mock
    private WarehouseDashboardService warehouseDashboardService;

    @InjectMocks
    private WarehouseDashboardController warehouseDashboardController;

    private WarehouseDashboardResponse mockDashboardResponse;

    @BeforeEach
    void setUp() {
        mockDashboardResponse = new WarehouseDashboardResponse();
        mockDashboardResponse.setTotalMedicines(150L);
        mockDashboardResponse.setTotalOutOfStock(5L);
        mockDashboardResponse.setTotalExpiringSoon(12L);
        mockDashboardResponse.setTotalImportValueMonth(BigDecimal.valueOf(50000000));
        mockDashboardResponse.setRecentImports(new ArrayList<>());
    }

    @Test
    void getDashboardStats_ShouldReturnSuccessResponse() {
        // Given
        when(warehouseDashboardService.getDashboardStats()).thenReturn(mockDashboardResponse);

        // When
        ResponseEntity<GlobalResponse<WarehouseDashboardResponse>> response = warehouseDashboardController
                .getDashboardStats();

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(150L, response.getBody().getData().getTotalMedicines());
        assertEquals(5L, response.getBody().getData().getTotalOutOfStock());

        verify(warehouseDashboardService, times(1)).getDashboardStats();
    }
}
