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
    
    /**
     * Get all medicine imports (non-paginated)
     * Endpoint: GET /api/imports/all
     */
    @GetMapping("/api/imports/all")
    @Operation(summary = "Get all imports", description = "Get all medicine imports without pagination")
    public ResponseEntity<GlobalResponse<List<MedicineImportListResponse>>> getAllImports() {
        List<MedicineImportListResponse> result = medicineImportService.getAllImports();
        GlobalResponse<List<MedicineImportListResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get import detail by ID
     * Endpoint: GET /api/imports/{id}
     */
    @GetMapping("/api/imports/{id}")
    @Operation(summary = "Get import detail", description = "Get detailed information about a specific medicine import including all details")
    public ResponseEntity<GlobalResponse<MedicineImportDetailResponse>> getImportDetail(@PathVariable int id) {
        MedicineImportDetailResponse result = medicineImportService.getImportDetail(id);
        GlobalResponse<MedicineImportDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        
        // Get staff ID from authenticated account
        int importerId = getStaffIdFromAccount(account);
        
        MedicineImportDetailResponse result = medicineImportService.createImport(request, bindingResult, importerId);
        GlobalResponse<MedicineImportDetailResponse> response = new GlobalResponse<>(result, Message.success, 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
        
        MedicineImportDetailResponse result = medicineImportService.updateImport(id, request, bindingResult);
        GlobalResponse<MedicineImportDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Delete medicine import
     * Endpoint: DELETE /api/imports/{id}
     */
    @DeleteMapping("/api/imports/{id}")
    @Operation(summary = "Delete import", description = "Delete a medicine import. This will also remove the corresponding inventory entries.")
    public ResponseEntity<GlobalResponse<Void>> deleteImport(@PathVariable int id) {
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
