package com.example.ooad.mapper;

import com.example.ooad.domain.entity.MedicineImport;
import com.example.ooad.dto.response.inventory.MedicineImportListResponse;

public class InventoryMapper {
    public static MedicineImportListResponse toMedicineImportListResponse(MedicineImport medicineImport) {
        MedicineImportListResponse response = new MedicineImportListResponse();
        response.setImportId(medicineImport.getImportId());
        response.setImportDate(medicineImport.getImportDate());
        response.setImporterName(
                medicineImport.getImporter() != null ? medicineImport.getImporter().getFullName() : null);
        response.setSupplier(medicineImport.getSupplier());
        response.setTotalQuantity(medicineImport.getTotalQuantity());
        response.setTotalValue(medicineImport.getTotalValue());
        return response;
    }
}
