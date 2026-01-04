package com.example.ooad.service.inventory.implementation;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.MedicineInventory;
import com.example.ooad.dto.request.inventory.InventoryFilterRequest;
import com.example.ooad.dto.response.inventory.InventoryDetailResponse;
import com.example.ooad.dto.response.inventory.InventoryItemResponse;
import com.example.ooad.dto.response.inventory.MedicineSelectionResponse;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.mapper.InventoryMapper;
import com.example.ooad.repository.MedicineImportRepository;
import com.example.ooad.repository.MedicineInventoryRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.service.inventory.interfaces.InventoryService;

@Service
public class InventoryServiceImplementation implements InventoryService {
    
    private final MedicineRepository medicineRepository;
    private final MedicineInventoryRepository medicineInventoryRepository;
    private final MedicineImportRepository medicineImportRepository;
    
    public InventoryServiceImplementation(
            MedicineRepository medicineRepository,
            MedicineInventoryRepository medicineInventoryRepository,
            MedicineImportRepository medicineImportRepository) {
        this.medicineRepository = medicineRepository;
        this.medicineInventoryRepository = medicineInventoryRepository;
        this.medicineImportRepository = medicineImportRepository;
    }

    @Override
    public Page<InventoryItemResponse> getInventoryList(InventoryFilterRequest filter) {
        // Get all medicines first (to include those with 0 stock)
        List<Medicine> allMedicines = medicineRepository.findAll();
        
        // Filter by keyword if provided
        if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {
            String keyword = filter.getKeyword().trim().toLowerCase();
            allMedicines = allMedicines.stream()
                .filter(m -> m.getMedicineName() != null && 
                        m.getMedicineName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        }
        
        // Map to response with inventory data
        List<InventoryItemResponse> items = allMedicines.stream()
            .map(medicine -> {
                int totalQuantity = medicineInventoryRepository.getTotalQuantityByMedicineId(medicine.getMedicineId());
                Date nearestExpiry = medicineInventoryRepository.getNearestExpiryDateByMedicineId(medicine.getMedicineId());
                return InventoryMapper.toInventoryItemResponse(medicine, totalQuantity, nearestExpiry);
            })
            .collect(Collectors.toList());
        
        // Filter out of stock if needed
        if (filter.getIncludeOutOfStock() == null || !filter.getIncludeOutOfStock()) {
            items = items.stream()
                .filter(item -> item.getTotalQuantity() > 0)
                .collect(Collectors.toList());
        }
        
        // Sort
        String sortBy = filter.getSortBy() != null ? filter.getSortBy() : "medicineId";
        boolean isDesc = "DESC".equalsIgnoreCase(filter.getSortType());
        
        items.sort((a, b) -> {
            int result = 0;
            switch (sortBy) {
                case "medicineName":
                    result = compareStrings(a.getMedicineName(), b.getMedicineName());
                    break;
                case "totalQuantity":
                    result = Integer.compare(a.getTotalQuantity(), b.getTotalQuantity());
                    break;
                case "nearestExpiryDate":
                    result = compareDates(a.getNearestExpiryDate(), b.getNearestExpiryDate());
                    break;
                case "medicineId":
                default:
                    result = Integer.compare(a.getMedicineId(), b.getMedicineId());
                    break;
            }
            return isDesc ? -result : result;
        });
        
        // Paginate
        int page = filter.getPage() != null ? filter.getPage() : 0;
        int size = filter.getSize() != null ? filter.getSize() : 10;
        int start = page * size;
        int end = Math.min(start + size, items.size());
        
        List<InventoryItemResponse> pageContent = start < items.size() 
            ? items.subList(start, end) 
            : List.of();
        
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(pageContent, pageable, items.size());
    }

    @Override
    public List<InventoryItemResponse> getAllInventoryItems() {
        return medicineRepository.findAll().stream()
            .map(medicine -> {
                int totalQuantity = medicineInventoryRepository.getTotalQuantityByMedicineId(medicine.getMedicineId());
                Date nearestExpiry = medicineInventoryRepository.getNearestExpiryDateByMedicineId(medicine.getMedicineId());
                return InventoryMapper.toInventoryItemResponse(medicine, totalQuantity, nearestExpiry);
            })
            .collect(Collectors.toList());
    }

    @Override
    public InventoryDetailResponse getInventoryDetail(int medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
            .orElseThrow(() -> new NotFoundException("Medicine not found with id: " + medicineId));
        
        int totalQuantity = medicineInventoryRepository.getTotalQuantityByMedicineId(medicineId);
        Date nearestExpiry = medicineInventoryRepository.getNearestExpiryDateByMedicineId(medicineId);
        List<MedicineInventory> batches = medicineInventoryRepository.findByMedicineId(medicineId);
        
        return InventoryMapper.toInventoryDetailResponse(medicine, totalQuantity, nearestExpiry, batches);
    }

    @Override
    public List<MedicineSelectionResponse> getMedicinesForSelection() {
        return medicineRepository.findAll().stream()
            .map(InventoryMapper::toMedicineSelectionResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getSuppliers() {
        return medicineImportRepository.findDistinctSuppliers();
    }
    
    // Helper methods for sorting
    private int compareStrings(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareToIgnoreCase(b);
    }
    
    private int compareDates(Date a, Date b) {
        if (a == null && b == null) return 0;
        if (a == null) return 1; // null dates go to end
        if (b == null) return -1;
        return a.compareTo(b);
    }
}
