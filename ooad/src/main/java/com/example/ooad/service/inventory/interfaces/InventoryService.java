package com.example.ooad.service.inventory.interfaces;

import java.util.List;
import java.util.Map;

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
    
    // ==================== APIs for Invoice/Dispensing ====================
    
    /**
     * Get available quantity for a specific medicine (excluding expired and near-expiry)
     * @param medicineId Medicine ID
     * @param minMonthsBeforeExpiry Minimum months before expiry to consider valid
     * @return Available quantity
     */
    int getAvailableQuantity(int medicineId, int minMonthsBeforeExpiry);
    
    /**
     * Check if medicine has sufficient quantity in stock
     * @param medicineId Medicine ID
     * @param requiredQuantity Required quantity
     * @param minMonthsBeforeExpiry Minimum months before expiry
     * @return true if sufficient, false otherwise
     */
    boolean checkAvailability(int medicineId, int requiredQuantity, int minMonthsBeforeExpiry);
    
    /**
     * Check availability for multiple medicines at once
     * @param requirements Map of medicineId to required quantity
     * @param minMonthsBeforeExpiry Minimum months before expiry
     * @return Map of medicineId to availability status (true/false)
     */
    Map<Integer, Boolean> checkBulkAvailability(Map<Integer, Integer> requirements, int minMonthsBeforeExpiry);
    
    /**
     * Deduct quantity from inventory when dispensing medicine (FEFO - First Expiry First Out)
     * This method reduces stock from batches starting with nearest expiry date
     * @param medicineId Medicine ID
     * @param quantity Quantity to deduct
     * @param minMonthsBeforeExpiry Minimum months before expiry to consider valid batches
     * @throws BadRequestException if insufficient stock
     */
    void deductInventory(int medicineId, int quantity, int minMonthsBeforeExpiry);
    
    /**
     * Deduct quantities for multiple medicines at once (transactional)
     * @param deductions Map of medicineId to quantity to deduct
     * @param minMonthsBeforeExpiry Minimum months before expiry
     * @throws BadRequestException if any medicine has insufficient stock
     */
    void deductBulkInventory(Map<Integer, Integer> deductions, int minMonthsBeforeExpiry);
    
    /**
     * Restore quantity to inventory (for cancellation/reversal scenarios)
     * This adds quantity back to the most recent batch
     * @param medicineId Medicine ID
     * @param quantity Quantity to restore
     */
    void restoreInventory(int medicineId, int quantity);
}
