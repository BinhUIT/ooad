package com.example.ooad.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.ooad.domain.compositekey.ImportDetailKey;
import com.example.ooad.domain.compositekey.MedicineInventoryKey;
import com.example.ooad.domain.entity.ImportDetail;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.MedicineImport;
import com.example.ooad.domain.entity.MedicineInventory;
import com.example.ooad.dto.request.inventory.ImportDetailRequest;
import com.example.ooad.dto.request.inventory.MedicineImportRequest;
import com.example.ooad.dto.response.inventory.ImportDetailResponse;
import com.example.ooad.dto.response.inventory.InventoryBatchResponse;
import com.example.ooad.dto.response.inventory.InventoryDetailResponse;
import com.example.ooad.dto.response.inventory.InventoryItemResponse;
import com.example.ooad.dto.response.inventory.MedicineImportDetailResponse;
import com.example.ooad.dto.response.inventory.MedicineImportListResponse;
import com.example.ooad.dto.response.inventory.MedicineSelectionResponse;

public class InventoryMapper {
    
    // ==================== Medicine Selection ====================
    
    public static MedicineSelectionResponse toMedicineSelectionResponse(Medicine medicine) {
        return new MedicineSelectionResponse(
            medicine.getMedicineId(),
            medicine.getMedicineName(),
            medicine.getUnit() != null ? medicine.getUnit().name() : null,
            medicine.getConcentration(),
            medicine.getManufacturer()
        );
    }
    
    // ==================== Inventory Item ====================
    
    public static InventoryItemResponse toInventoryItemResponse(Medicine medicine, int totalQuantity, Date nearestExpiryDate) {
        InventoryItemResponse response = new InventoryItemResponse();
        response.setMedicineId(medicine.getMedicineId());
        response.setMedicineName(medicine.getMedicineName());
        response.setUnit(medicine.getUnit() != null ? medicine.getUnit().name() : null);
        response.setTotalQuantity(totalQuantity);
        response.setNearestExpiryDate(nearestExpiryDate);
        response.setManufacturer(medicine.getManufacturer());
        response.setConcentration(medicine.getConcentration());
        response.setStorageCondition(medicine.getStorageCondition() != null ? medicine.getStorageCondition().name() : null);
        return response;
    }
    
    // ==================== Inventory Batch ====================
    
    public static InventoryBatchResponse toInventoryBatchResponse(MedicineInventory inventory) {
        InventoryBatchResponse response = new InventoryBatchResponse();
        response.setImportId(inventory.getMedicineImport().getImportId());
        response.setMedicineId(inventory.getMedicine().getMedicineId());
        response.setMedicineName(inventory.getMedicine().getMedicineName());
        response.setUnit(inventory.getMedicine().getUnit() != null ? inventory.getMedicine().getUnit().name() : null);
        response.setQuantityInStock(inventory.getQuantityInStock());
        response.setExpiryDate(inventory.getExpiryDate());
        response.setImportPrice(inventory.getImportPrice());
        response.setImportDate(inventory.getMedicineImport().getImportDate());
        response.setSupplier(inventory.getMedicineImport().getSupplier());
        response.setManufacturer(inventory.getMedicine().getManufacturer());
        response.setConcentration(inventory.getMedicine().getConcentration());
        response.setStorageCondition(inventory.getMedicine().getStorageCondition() != null 
            ? inventory.getMedicine().getStorageCondition().name() : null);
        return response;
    }
    
    // ==================== Inventory Detail ====================
    
    public static InventoryDetailResponse toInventoryDetailResponse(Medicine medicine, int totalQuantity, 
            Date nearestExpiryDate, List<MedicineInventory> batches) {
        InventoryDetailResponse response = new InventoryDetailResponse();
        response.setMedicineId(medicine.getMedicineId());
        response.setMedicineName(medicine.getMedicineName());
        response.setUnit(medicine.getUnit() != null ? medicine.getUnit().name() : null);
        response.setConcentration(medicine.getConcentration());
        response.setForm(medicine.getForm());
        response.setManufacturer(medicine.getManufacturer());
        response.setUsageInstructions(medicine.getUsageInstructions());
        response.setImage(medicine.getImage());
        response.setStorageCondition(medicine.getStorageCondition() != null ? medicine.getStorageCondition().name() : null);
        response.setTotalQuantity(totalQuantity);
        response.setNearestExpiryDate(nearestExpiryDate);
        response.setBatches(batches.stream()
            .map(InventoryMapper::toInventoryBatchResponse)
            .collect(Collectors.toList()));
        return response;
    }
    
    // ==================== Medicine Import List ====================
    
    public static MedicineImportListResponse toMedicineImportListResponse(MedicineImport medicineImport) {
        MedicineImportListResponse response = new MedicineImportListResponse();
        response.setImportId(medicineImport.getImportId());
        response.setImportDate(medicineImport.getImportDate());
        response.setImporterName(medicineImport.getImporter() != null ? medicineImport.getImporter().getFullName() : null);
        response.setSupplier(medicineImport.getSupplier());
        response.setTotalQuantity(medicineImport.getTotalQuantity());
        response.setTotalValue(medicineImport.getTotalValue());
        return response;
    }
    
    // ==================== Medicine Import Detail ====================
    
    public static MedicineImportDetailResponse toMedicineImportDetailResponse(MedicineImport medicineImport, 
            List<ImportDetail> details) {
        MedicineImportDetailResponse response = new MedicineImportDetailResponse();
        response.setImportId(medicineImport.getImportId());
        response.setImportDate(medicineImport.getImportDate());
        response.setImporterId(medicineImport.getImporter() != null ? medicineImport.getImporter().getStaffId() : 0);
        response.setImporterName(medicineImport.getImporter() != null ? medicineImport.getImporter().getFullName() : null);
        response.setSupplier(medicineImport.getSupplier());
        response.setTotalQuantity(medicineImport.getTotalQuantity());
        response.setTotalValue(medicineImport.getTotalValue());
        response.setDetails(details.stream()
            .map(InventoryMapper::toImportDetailResponse)
            .collect(Collectors.toList()));
        return response;
    }
    
    // ==================== Import Detail ====================
    
    public static ImportDetailResponse toImportDetailResponse(ImportDetail detail) {
        ImportDetailResponse response = new ImportDetailResponse();
        response.setMedicineId(detail.getMedicine().getMedicineId());
        response.setMedicineName(detail.getMedicine().getMedicineName());
        response.setUnit(detail.getMedicine().getUnit() != null ? detail.getMedicine().getUnit().name() : null);
        response.setQuantity(detail.getQuantity());
        response.setImportPrice(BigDecimal.valueOf(detail.getImportPrice()));
        response.setTotalAmount(BigDecimal.valueOf(detail.getImportPrice()).multiply(BigDecimal.valueOf(detail.getQuantity())));
        response.setExpiryDate(detail.getExpiryDate());
        return response;
    }
    
    // ==================== Create Entities ====================
    
    public static MedicineImport toMedicineImportEntity(MedicineImportRequest request) {
        MedicineImport entity = new MedicineImport();
        entity.setSupplier(request.getSupplier());
        entity.setImportDate(request.getImportDate());
        entity.setTotalQuantity(0);
        entity.setTotalValue(BigDecimal.ZERO);
        return entity;
    }
    
    public static ImportDetail toImportDetailEntity(ImportDetailRequest request, MedicineImport medicineImport, Medicine medicine) {
        ImportDetail detail = new ImportDetail();
        ImportDetailKey key = new ImportDetailKey(medicineImport.getImportId(), medicine.getMedicineId());
        detail.setImportDetailId(key);
        detail.setMedicineImport(medicineImport);
        detail.setMedicine(medicine);
        detail.setQuantity(request.getQuantity());
        detail.setImportPrice(request.getImportPrice().floatValue());
        detail.setExpiryDate(request.getExpiryDate());
        return detail;
    }
    
    public static MedicineInventory toMedicineInventoryEntity(ImportDetailRequest request, MedicineImport medicineImport, Medicine medicine) {
        MedicineInventory inventory = new MedicineInventory();
        MedicineInventoryKey key = new MedicineInventoryKey(medicineImport.getImportId(), medicine.getMedicineId());
        inventory.setMedicineInventoryId(key);
        inventory.setMedicineImport(medicineImport);
        inventory.setMedicine(medicine);
        inventory.setQuantityInStock(request.getQuantity());
        inventory.setImportPrice(request.getImportPrice());
        inventory.setExpiryDate(request.getExpiryDate());
        return inventory;
    }
    
    // ==================== Update Entities ====================
    
    public static void updateMedicineImportEntity(MedicineImport entity, MedicineImportRequest request) {
        if (request.getSupplier() != null) {
            entity.setSupplier(request.getSupplier());
        }
        if (request.getImportDate() != null) {
            entity.setImportDate(request.getImportDate());
        }
    }
}