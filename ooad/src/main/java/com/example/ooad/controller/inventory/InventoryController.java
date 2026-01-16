package com.example.ooad.controller.inventory;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.request.inventory.InventoryFilterRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.inventory.InventoryDetailResponse;
import com.example.ooad.dto.response.inventory.InventoryItemResponse;
import com.example.ooad.dto.response.inventory.MedicineSelectionResponse;
import com.example.ooad.service.inventory.interfaces.InventoryService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController

@Tag(name = "Inventory Management", description = "APIs for managing medicine inventory")
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    
    /**
     * Get paginated list of inventory items
     * Endpoint: GET /api/inventory
     */
    @GetMapping("/api/inventory")
    @Operation(summary = "Get inventory list", description = "Get paginated list of medicine inventory with search and filter options")
    public ResponseEntity<GlobalResponse<Page<InventoryItemResponse>>> getInventoryList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean includeOutOfStock) {
        
        InventoryFilterRequest filter = new InventoryFilterRequest();
        if (page != null) filter.setPage(page);
        if (size != null) filter.setSize(size);
        if (sortBy != null) filter.setSortBy(sortBy);
        if (sortType != null) filter.setSortType(sortType);
        if (keyword != null) filter.setKeyword(keyword);
        if (includeOutOfStock != null) filter.setIncludeOutOfStock(includeOutOfStock);
        
        Page<InventoryItemResponse> result = inventoryService.getInventoryList(filter);
        GlobalResponse<Page<InventoryItemResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get all inventory items (non-paginated)
     * Endpoint: GET /api/inventory/all
     */
    @GetMapping("/api/inventory/all")
    @Operation(summary = "Get all inventory items", description = "Get all medicine inventory items without pagination")
    public ResponseEntity<GlobalResponse<List<InventoryItemResponse>>> getAllInventoryItems() {
        List<InventoryItemResponse> result = inventoryService.getAllInventoryItems();
        GlobalResponse<List<InventoryItemResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get inventory detail by medicine ID
     * Endpoint: GET /api/inventory/{id}
     */
    @GetMapping("/api/inventory/{id}")
    @Operation(summary = "Get inventory detail", description = "Get detailed inventory information for a specific medicine including all batches")
    public ResponseEntity<GlobalResponse<InventoryDetailResponse>> getInventoryDetail(@PathVariable int id) {
        InventoryDetailResponse result = inventoryService.getInventoryDetail(id);
        GlobalResponse<InventoryDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get medicines for dropdown selection
     * Endpoint: GET /api/medicines/selection
     */
    @GetMapping("/api/medicines/selection")
    @Operation(summary = "Get medicines for selection", description = "Get list of medicines for dropdown selection when creating import")
    public ResponseEntity<GlobalResponse<List<MedicineSelectionResponse>>> getMedicinesForSelection() {
        List<MedicineSelectionResponse> result = inventoryService.getMedicinesForSelection();
        GlobalResponse<List<MedicineSelectionResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get list of suppliers
     * Endpoint: GET /api/suppliers
     */
    @GetMapping("/api/suppliers")
    @Operation(summary = "Get suppliers", description = "Get list of distinct supplier names for dropdown selection")
    public ResponseEntity<GlobalResponse<List<String>>> getSuppliers() {
        List<String> result = inventoryService.getSuppliers();
        GlobalResponse<List<String>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // ============== APIs for Invoice/Dispensing Integration ==============
    
    /**
     * Get available quantity for a medicine
     * Endpoint: GET /api/inventory/{medicineId}/available
     */
    @GetMapping("/api/inventory/{medicineId}/available")
    @Operation(summary = "Get available quantity", 
               description = "Get available quantity of a medicine considering expiry date. Default minimum 3 months before expiry.")
    public ResponseEntity<GlobalResponse<Integer>> getAvailableQuantity(
            @PathVariable int medicineId,
            @RequestParam(required = false, defaultValue = "3") int minMonthsBeforeExpiry) {
        int quantity = inventoryService.getAvailableQuantity(medicineId, minMonthsBeforeExpiry);
        GlobalResponse<Integer> response = new GlobalResponse<>(quantity, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Check availability for a single medicine
     * Endpoint: GET /api/inventory/{medicineId}/check-availability
     */
    @GetMapping("/api/inventory/{medicineId}/check-availability")
    @Operation(summary = "Check medicine availability", 
               description = "Check if required quantity is available for a medicine")
    public ResponseEntity<GlobalResponse<Boolean>> checkAvailability(
            @PathVariable int medicineId,
            @RequestParam int requiredQuantity,
            @RequestParam(required = false, defaultValue = "3") int minMonthsBeforeExpiry) {
        boolean available = inventoryService.checkAvailability(medicineId, requiredQuantity, minMonthsBeforeExpiry);
        GlobalResponse<Boolean> response = new GlobalResponse<>(available, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Check bulk availability for multiple medicines
     * Endpoint: POST /api/inventory/check-bulk-availability
     */
    @PostMapping("/api/inventory/check-bulk-availability")
    @Operation(summary = "Check bulk availability", 
               description = "Check availability for multiple medicines at once. Request body: Map of medicineId to required quantity")
    public ResponseEntity<GlobalResponse<Map<Integer, Boolean>>> checkBulkAvailability(
            @RequestBody Map<Integer, Integer> medicineQuantities,
            @RequestParam(required = false, defaultValue = "3") int minMonthsBeforeExpiry) {
        Map<Integer, Boolean> result = inventoryService.checkBulkAvailability(medicineQuantities, minMonthsBeforeExpiry);
        GlobalResponse<Map<Integer, Boolean>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Deduct inventory when dispensing medicine (FEFO - First Expiry First Out)
     * Endpoint: POST /api/inventory/{medicineId}/deduct
     */
    @PostMapping("/api/inventory/{medicineId}/deduct")
    @Operation(summary = "Deduct inventory", 
               description = "Deduct quantity from inventory using FEFO (First Expiry First Out) logic")
    public ResponseEntity<GlobalResponse<String>> deductInventory(
            @PathVariable int medicineId,
            @RequestParam int quantity,
            @RequestParam(required = false, defaultValue = "3") int minMonthsBeforeExpiry) {
        inventoryService.deductInventory(medicineId, quantity, minMonthsBeforeExpiry);
        GlobalResponse<String> response = new GlobalResponse<>("Đã trừ số lượng thuốc thành công", Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Deduct inventory for multiple medicines at once
     * Endpoint: POST /api/inventory/deduct-bulk
     */
    @PostMapping("/api/inventory/deduct-bulk")
    @Operation(summary = "Deduct bulk inventory", 
               description = "Deduct quantities for multiple medicines at once. All or nothing - if any medicine is insufficient, nothing is deducted.")
    public ResponseEntity<GlobalResponse<String>> deductBulkInventory(
            @RequestBody Map<Integer, Integer> medicineQuantities,
            @RequestParam(required = false, defaultValue = "3") int minMonthsBeforeExpiry) {
        inventoryService.deductBulkInventory(medicineQuantities, minMonthsBeforeExpiry);
        GlobalResponse<String> response = new GlobalResponse<>("Đã trừ số lượng thuốc thành công", Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Restore inventory (for cancellation/return)
     * Endpoint: POST /api/inventory/{medicineId}/restore
     */
    @PostMapping("/api/inventory/{medicineId}/restore")
    @Operation(summary = "Restore inventory", 
               description = "Restore quantity to inventory when order is cancelled or medicine is returned")
    public ResponseEntity<GlobalResponse<String>> restoreInventory(
            @PathVariable int medicineId,
            @RequestParam int quantity) {
        inventoryService.restoreInventory(medicineId, quantity);
        GlobalResponse<String> response = new GlobalResponse<>("Đã khôi phục số lượng thuốc thành công", Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
