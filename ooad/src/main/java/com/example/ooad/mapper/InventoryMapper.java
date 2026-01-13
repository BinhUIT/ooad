package com.example.ooad.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
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
    
    /**
     * Convert MedicineImport to MedicineImportDetailResponse with editable status
     * @param medicineImport The medicine import entity
     * @param details List of import details
     * @param inventoryMap Map of (medicineId -> MedicineInventory) for this import
     * @return MedicineImportDetailResponse with editable status
     */
    public static MedicineImportDetailResponse toMedicineImportDetailResponse(MedicineImport medicineImport, 
            List<ImportDetail> details, java.util.Map<Integer, MedicineInventory> inventoryMap) {
        MedicineImportDetailResponse response = new MedicineImportDetailResponse();
        response.setImportId(medicineImport.getImportId());
        response.setImportDate(medicineImport.getImportDate());
        response.setImporterId(medicineImport.getImporter() != null ? medicineImport.getImporter().getStaffId() : 0);
        response.setImporterName(medicineImport.getImporter() != null ? medicineImport.getImporter().getFullName() : null);
        response.setSupplier(medicineImport.getSupplier());
        response.setTotalQuantity(medicineImport.getTotalQuantity());
        response.setTotalValue(medicineImport.getTotalValue());
        
        // Check if import is within 3 days (for WarehouseStaff edit restriction)
        boolean isWithin3Days = isImportWithin3Days(medicineImport.getImportDate());
        
        // Convert details with inventory info
        List<ImportDetailResponse> detailResponses = details.stream()
            .map(detail -> {
                MedicineInventory inventory = inventoryMap != null ? 
                    inventoryMap.get(detail.getMedicine().getMedicineId()) : null;
                return toImportDetailResponse(detail, inventory, isWithin3Days);
            })
            .collect(Collectors.toList());
        response.setDetails(detailResponses);
        
        // Import is editable if within 3 days (regardless of individual items sold status)
        // Individual items that are sold will have their own editable=false
        // This allows WarehouseStaff to still edit/delete items that haven't been sold
        response.setEditable(isWithin3Days);
        
        return response;
    }
    
    /**
     * Check if import date is within 3 days from today
     */
    private static boolean isImportWithin3Days(Date importDate) {
        if (importDate == null) return false;
        LocalDate importLocalDate = importDate.toLocalDate();
        LocalDate today = LocalDate.now();
        LocalDate threeDaysAgo = today.minusDays(3);
        return !importLocalDate.isBefore(threeDaysAgo);
    }
    
    /**
     * Legacy method for backward compatibility
     */
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
        response.setEditable(true); // Assume editable for backward compatibility
        return response;
    }
    
    // ==================== Import Detail ====================
    
    /**
     * Convert ImportDetail to ImportDetailResponse with editable status and 3-day check
     * @param detail The import detail entity
     * @param inventory The corresponding inventory entity (can be null)
     * @param isWithin3Days Whether the import is within 3 days from today
     * @return ImportDetailResponse with quantity in stock and editable status
     */
    public static ImportDetailResponse toImportDetailResponse(ImportDetail detail, MedicineInventory inventory, boolean isWithin3Days) {
        ImportDetailResponse response = new ImportDetailResponse();
        response.setMedicineId(detail.getMedicine().getMedicineId());
        response.setMedicineName(detail.getMedicine().getMedicineName());
        response.setUnit(detail.getMedicine().getUnit() != null ? detail.getMedicine().getUnit().name() : null);
        response.setQuantity(detail.getQuantity()); // Import quantity (static)
        response.setImportPrice(BigDecimal.valueOf(detail.getImportPrice()));
        response.setTotalAmount(BigDecimal.valueOf(detail.getImportPrice()).multiply(BigDecimal.valueOf(detail.getQuantity())));
        response.setExpiryDate(detail.getExpiryDate());
        
        // Set quantity in stock and calculate editable status
        if (inventory != null) {
            response.setQuantityInStock(inventory.getQuantityInStock());
            // Check if items have been sold
            boolean notSold = inventory.getQuantityInStock() == detail.getQuantity();
            // Editable only if: no items sold AND within 3 days
            boolean isEditable = notSold && isWithin3Days;
            response.setEditable(isEditable);
            
            if (!notSold) {
                response.setStatusMessage("Đã bán, không được sửa/xóa");
            } else if (!isWithin3Days) {
                response.setStatusMessage("Quá 3 ngày, không được sửa/xóa");
            } else {
                response.setStatusMessage("Có thể sửa/xóa");
            }
        } else {
            // No inventory found - data error, don't allow editing for safety
            response.setQuantityInStock(0);
            response.setEditable(false);
            response.setStatusMessage("Lỗi dữ liệu kho");
        }
        
        return response;
    }
    
    /**
     * Convert ImportDetail to ImportDetailResponse with editable status (default: check 3-day rule)
     */
    public static ImportDetailResponse toImportDetailResponse(ImportDetail detail, MedicineInventory inventory) {
        // Assume within 3 days for backward compatibility
        return toImportDetailResponse(detail, inventory, true);
    }
    
    /**
     * Legacy method for backward compatibility (without inventory - assumes editable)
     */
    public static ImportDetailResponse toImportDetailResponse(ImportDetail detail) {
        ImportDetailResponse response = new ImportDetailResponse();
        response.setMedicineId(detail.getMedicine().getMedicineId());
        response.setMedicineName(detail.getMedicine().getMedicineName());
        response.setUnit(detail.getMedicine().getUnit() != null ? detail.getMedicine().getUnit().name() : null);
        response.setQuantity(detail.getQuantity());
        response.setImportPrice(BigDecimal.valueOf(detail.getImportPrice()));
        response.setTotalAmount(BigDecimal.valueOf(detail.getImportPrice()).multiply(BigDecimal.valueOf(detail.getQuantity())));
        response.setExpiryDate(detail.getExpiryDate());
        response.setQuantityInStock(detail.getQuantity()); // Assume full stock
        response.setEditable(true);
        response.setStatusMessage("Có thể sửa/xóa");
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