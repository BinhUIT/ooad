package com.example.ooad.controller.inventory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
