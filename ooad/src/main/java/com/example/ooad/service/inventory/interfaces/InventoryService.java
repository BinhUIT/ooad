package com.example.ooad.service.inventory.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.ooad.dto.request.inventory.InventoryFilterRequest;
import com.example.ooad.dto.response.inventory.InventoryDetailResponse;
import com.example.ooad.dto.response.inventory.InventoryItemResponse;
import com.example.ooad.dto.response.inventory.MedicineSelectionResponse;

public interface InventoryService {
    
    /**
     * Get paginated list of inventory items with search and filter
     * @param filter Filter request containing pagination, search and filter parameters
     * @return Page of inventory items
     */
    Page<InventoryItemResponse> getInventoryList(InventoryFilterRequest filter);
    
    /**
     * Get all inventory items (non-paginated)
     * @return List of all inventory items
     */
    List<InventoryItemResponse> getAllInventoryItems();
    
    /**
     * Get detailed information about a specific medicine in inventory
     * @param medicineId Medicine ID
     * @return Inventory detail including all batches
     */
    InventoryDetailResponse getInventoryDetail(int medicineId);
    
    /**
     * Get list of medicines for dropdown selection when creating import
     * @return List of medicine selection options
     */
    List<MedicineSelectionResponse> getMedicinesForSelection();
    
    /**
     * Get list of distinct suppliers for dropdown selection
     * @return List of supplier names
     */
    List<String> getSuppliers();
}
