package com.example.ooad.service.inventory.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.example.ooad.dto.request.inventory.MedicineImportFilterRequest;
import com.example.ooad.dto.request.inventory.MedicineImportRequest;
import com.example.ooad.dto.response.inventory.MedicineImportDetailResponse;
import com.example.ooad.dto.response.inventory.MedicineImportListResponse;

public interface MedicineImportService {
    
    /**
     * Get paginated list of medicine imports with search and filter
     * @param filter Filter request containing pagination, search and filter parameters
     * @return Page of medicine import list items
     */
    Page<MedicineImportListResponse> getImportList(MedicineImportFilterRequest filter);
    
    /**
     * Get all medicine imports (non-paginated)
     * @return List of all medicine imports
     */
    List<MedicineImportListResponse> getAllImports();
    
    /**
     * Get detailed information about a specific medicine import
     * @param importId Import ID
     * @return Import detail including all import details
     */
    MedicineImportDetailResponse getImportDetail(int importId);
    
    /**
     * Create a new medicine import with details
     * This will also update the medicine inventory
     * @param request Import request containing import info and details
     * @param bindingResult Validation result
     * @param importerId Staff ID of the person creating the import
     * @return Created import detail response
     */
    MedicineImportDetailResponse createImport(MedicineImportRequest request, BindingResult bindingResult, int importerId);
    
    /**
     * Update an existing medicine import
     * This will recalculate inventory (subtract old quantities, add new quantities)
     * @param importId Import ID to update
     * @param request Import request containing updated info and details
     * @param bindingResult Validation result
     * @return Updated import detail response
     */
    MedicineImportDetailResponse updateImport(int importId, MedicineImportRequest request, BindingResult bindingResult);
    
    /**
     * Delete a medicine import
     * This will also remove the corresponding inventory entries
     * @param importId Import ID to delete
     */
    void deleteImport(int importId);
}
