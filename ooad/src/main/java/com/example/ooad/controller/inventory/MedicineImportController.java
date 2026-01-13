package com.example.ooad.controller.inventory;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.request.inventory.MedicineImportFilterRequest;
import com.example.ooad.dto.request.inventory.MedicineImportRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.inventory.MedicineImportDetailResponse;
import com.example.ooad.dto.response.inventory.MedicineImportListResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.service.inventory.interfaces.MedicineImportService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Medicine Import Management", description = "APIs for managing medicine imports")
public class MedicineImportController {
    
    private final MedicineImportService medicineImportService;
    private final StaffRepository staffRepository;
    
    public MedicineImportController(MedicineImportService medicineImportService, StaffRepository staffRepository) {
        this.medicineImportService = medicineImportService;
        this.staffRepository = staffRepository;
    }
    
    // ==================== ADMIN Endpoints ====================
    
    /**
     * Get paginated list of medicine imports (Admin)
     * Endpoint: GET /admin/imports
     */
    @GetMapping("/admin/imports")
    @Operation(summary = "Get import list (Admin)", description = "Get paginated list of medicine imports with search and filter options")
    public ResponseEntity<GlobalResponse<Page<MedicineImportListResponse>>> getImportListAdmin(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) Date fromDate,
            @RequestParam(required = false) Date toDate,
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) String keyword) {
        
        return getImportListResponse(page, size, sortBy, sortType, fromDate, toDate, supplierName, keyword);
    }
    
    /**
     * Get all medicine imports (Admin)
     * Endpoint: GET /admin/imports/all
     */
    @GetMapping("/admin/imports/all")
    @Operation(summary = "Get all imports (Admin)", description = "Get all medicine imports without pagination")
    public ResponseEntity<GlobalResponse<List<MedicineImportListResponse>>> getAllImportsAdmin() {
        return getAllImportsResponse();
    }
    
    /**
     * Get import detail by ID (Admin)
     * Endpoint: GET /admin/imports/{id}
     */
    @GetMapping("/admin/imports/{id}")
    @Operation(summary = "Get import detail (Admin)", description = "Get detailed information about a specific medicine import")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> getImportDetailAdmin(@PathVariable int id) {
        return getImportDetailResponse(id);
    }
    
    /**
     * Create new medicine import (Admin)
     * Endpoint: POST /admin/imports
     */
    @PostMapping("/admin/imports")
    @Operation(summary = "Create import (Admin)", description = "Create a new medicine import with details")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> createImportAdmin(
            @RequestBody @Valid MedicineImportRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal Account account) {
        return createImportResponse(request, bindingResult, account);
    }
    
    /**
     * Update existing medicine import (Admin)
     * Endpoint: PUT /admin/imports/{id}
     */
    @PutMapping("/admin/imports/{id}")
    @Operation(summary = "Update import (Admin)", description = "Update an existing medicine import")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> updateImportAdmin(
            @PathVariable int id,
            @RequestBody @Valid MedicineImportRequest request,
            BindingResult bindingResult) {
        return updateImportResponse(id, request, bindingResult);
    }
    
    /**
     * Delete medicine import (Admin)
     * Endpoint: DELETE /admin/imports/{id}
     */
    @DeleteMapping("/admin/imports/{id}")
    @Operation(summary = "Delete import (Admin)", description = "Delete a medicine import")
    public ResponseEntity<GlobalResponse<Void>> deleteImportAdmin(@PathVariable int id) {
        return deleteImportResponse(id);
    }
    
    // ==================== WAREHOUSE STAFF Endpoints ====================
    
    /**
     * Get paginated list of medicine imports (Warehouse Staff)
     * Endpoint: GET /store_keeper/imports
     */
    @GetMapping("/store_keeper/imports")
    @Operation(summary = "Get import list (Warehouse Staff)", description = "Get paginated list of medicine imports")
    public ResponseEntity<GlobalResponse<Page<MedicineImportListResponse>>> getImportListWarehouse(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) Date fromDate,
            @RequestParam(required = false) Date toDate,
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) String keyword) {
        
        return getImportListResponse(page, size, sortBy, sortType, fromDate, toDate, supplierName, keyword);
    }
    
    /**
     * Get all medicine imports (Warehouse Staff)
     * Endpoint: GET /store_keeper/imports/all
     */
    @GetMapping("/store_keeper/imports/all")
    @Operation(summary = "Get all imports (Warehouse Staff)", description = "Get all medicine imports without pagination")
    public ResponseEntity<GlobalResponse<List<MedicineImportListResponse>>> getAllImportsWarehouse() {
        return getAllImportsResponse();
    }
    
    /**
     * Get import detail by ID (Warehouse Staff)
     * Endpoint: GET /store_keeper/imports/{id}
     */
    @GetMapping("/store_keeper/imports/{id}")
    @Operation(summary = "Get import detail (Warehouse Staff)", description = "Get detailed information about a specific medicine import")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> getImportDetailWarehouse(@PathVariable int id) {
        return getImportDetailResponse(id);
    }
    
    /**
     * Create new medicine import (Warehouse Staff)
     * Endpoint: POST /store_keeper/imports
     */
    @PostMapping("/store_keeper/imports")
    @Operation(summary = "Create import (Warehouse Staff)", description = "Create a new medicine import with details")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> createImportWarehouse(
            @RequestBody @Valid MedicineImportRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal Account account) {
        return createImportResponse(request, bindingResult, account);
    }
    
    /**
     * Update existing medicine import (Warehouse Staff)
     * Endpoint: PUT /store_keeper/imports/{id}
     */
    @PutMapping("/store_keeper/imports/{id}")
    @Operation(summary = "Update import (Warehouse Staff)", description = "Update an existing medicine import")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> updateImportWarehouse(
            @PathVariable int id,
            @RequestBody @Valid MedicineImportRequest request,
            BindingResult bindingResult) {
        return updateImportResponse(id, request, bindingResult);
    }
    
    /**
     * Delete medicine import (Warehouse Staff)
     * Endpoint: DELETE /store_keeper/imports/{id}
     */
    @DeleteMapping("/store_keeper/imports/{id}")
    @Operation(summary = "Delete import (Warehouse Staff)", description = "Delete a medicine import")
    public ResponseEntity<GlobalResponse<Void>> deleteImportWarehouse(@PathVariable int id) {
        return deleteImportResponse(id);
    }
    
    // ==================== Legacy API Endpoints (for backward compatibility) ====================
    
    /**
     * Get paginated list of medicine imports
     * Endpoint: GET /api/imports
     */
    @GetMapping("/api/imports")
    @Operation(summary = "Get import list", description = "Get paginated list of medicine imports with search and filter options")
    public ResponseEntity<GlobalResponse<Page<MedicineImportListResponse>>> getImportList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) Date fromDate,
            @RequestParam(required = false) Date toDate,
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) String keyword) {
        
        return getImportListResponse(page, size, sortBy, sortType, fromDate, toDate, supplierName, keyword);
    }
    
    /**
     * Get all medicine imports (non-paginated)
     * Endpoint: GET /api/imports/all
     */
    @GetMapping("/api/imports/all")
    @Operation(summary = "Get all imports", description = "Get all medicine imports without pagination")
    public ResponseEntity<GlobalResponse<List<MedicineImportListResponse>>> getAllImports() {
        return getAllImportsResponse();
    }
    
    /**
     * Get import detail by ID
     * Endpoint: GET /api/imports/{id}
     */
    @GetMapping("/api/imports/{id}")
    @Operation(summary = "Get import detail", description = "Get detailed information about a specific medicine import including all details")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> getImportDetail(@PathVariable int id) {
        return getImportDetailResponse(id);
    }
    
    /**
     * Create new medicine import
     * Endpoint: POST /api/imports
     */
    @PostMapping("/api/imports")
    @Operation(summary = "Create import", description = "Create a new medicine import with details. This will also update the medicine inventory.")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> createImport(
            @RequestBody @Valid MedicineImportRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal Account account) {
        return createImportResponse(request, bindingResult, account);
    }
    
    /**
     * Update existing medicine import
     * Endpoint: PUT /api/imports/{id}
     */
    @PutMapping("/api/imports/{id}")
    @Operation(summary = "Update import", description = "Update an existing medicine import. This will recalculate inventory (subtract old, add new).")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> updateImport(
            @PathVariable int id,
            @RequestBody @Valid MedicineImportRequest request,
            BindingResult bindingResult) {
        return updateImportResponse(id, request, bindingResult);
    }
    
    /**
     * Delete medicine import
     * Endpoint: DELETE /api/imports/{id}
     */
    @DeleteMapping("/api/imports/{id}")
    @Operation(summary = "Delete import", description = "Delete a medicine import. This will also remove the corresponding inventory entries.")
    public ResponseEntity<GlobalResponse<Void>> deleteImport(@PathVariable int id) {
        return deleteImportResponse(id);
    }
    
    // ==================== Private Helper Methods ====================
    
    private ResponseEntity<GlobalResponse<Page<MedicineImportListResponse>>> getImportListResponse(
            Integer page, Integer size, String sortBy, String sortType,
            Date fromDate, Date toDate, String supplierName, String keyword) {
        
        MedicineImportFilterRequest filter = new MedicineImportFilterRequest();
        if (page != null) filter.setPage(page);
        if (size != null) filter.setSize(size);
        if (sortBy != null) filter.setSortBy(sortBy);
        if (sortType != null) filter.setSortType(sortType);
        if (fromDate != null) filter.setFromDate(fromDate);
        if (toDate != null) filter.setToDate(toDate);
        if (supplierName != null) filter.setSupplierName(supplierName);
        if (keyword != null) filter.setKeyword(keyword);
        
        Page<MedicineImportListResponse> result = medicineImportService.getImportList(filter);
        GlobalResponse<Page<MedicineImportListResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    private ResponseEntity<GlobalResponse<List<MedicineImportListResponse>>> getAllImportsResponse() {
        List<MedicineImportListResponse> result = medicineImportService.getAllImports();
        GlobalResponse<List<MedicineImportListResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    private ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> getImportDetailResponse(int id) {
        MedicineImportDetailResponse result = medicineImportService.getImportDetail(id);
        GlobalResponse<MedicineImportDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    private ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> createImportResponse(
            MedicineImportRequest request, BindingResult bindingResult, Account account) {
        int importerId = getStaffIdFromAccount(account);
        MedicineImportDetailResponse result = medicineImportService.createImport(request, bindingResult, importerId);
        GlobalResponse<MedicineImportDetailResponse> response = new GlobalResponse<>(result, Message.success, 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    private ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> updateImportResponse(
            int id, MedicineImportRequest request, BindingResult bindingResult) {
        MedicineImportDetailResponse result = medicineImportService.updateImport(id, request, bindingResult);
        GlobalResponse<MedicineImportDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    private ResponseEntity<GlobalResponse<Void>> deleteImportResponse(int id) {
        medicineImportService.deleteImport(id);
        GlobalResponse<Void> response = new GlobalResponse<>(null, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Helper method to get staff ID from authenticated account
     */
    private int getStaffIdFromAccount(Account account) {
        if (account == null) {
            throw new BadRequestException("Unable to identify user from authentication");
        }
        Staff staff = staffRepository.findByAccountId(account.getAccountId())
            .orElseThrow(() -> new BadRequestException("Staff not found for this account"));
        return staff.getStaffId();
    }
}
