package com.example.ooad.controller.inventory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.inventory.WarehouseDashboardResponse;
import com.example.ooad.service.inventory.interfaces.WarehouseDashboardService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/warehouse/dashboard")
@Tag(name = "Warehouse Dashboard", description = "APIs for warehouse dashboard statistics")
public class WarehouseDashboardController {

    private final WarehouseDashboardService dashboardService;

    public WarehouseDashboardController(WarehouseDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<GlobalResponse<WarehouseDashboardResponse>> getDashboardStats() {
        WarehouseDashboardResponse stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(new GlobalResponse<>(stats, Message.success, HttpStatus.OK.value()));
    }
}
