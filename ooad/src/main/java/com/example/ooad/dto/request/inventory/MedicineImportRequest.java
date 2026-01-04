package com.example.ooad.dto.request.inventory;

import java.sql.Date;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MedicineImportRequest {
    
    @NotBlank(message = "Supplier is required")
    @Size(max = 100, message = "Supplier name must not exceed 100 characters")
    private String supplier;
    
    @NotNull(message = "Import date is required")
    private Date importDate;
    
    @NotEmpty(message = "Import details are required")
    @Valid
    private List<ImportDetailRequest> details;

    public MedicineImportRequest() {
    }

    public MedicineImportRequest(String supplier, Date importDate, List<ImportDetailRequest> details) {
        this.supplier = supplier;
        this.importDate = importDate;
        this.details = details;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public List<ImportDetailRequest> getDetails() {
        return details;
    }

    public void setDetails(List<ImportDetailRequest> details) {
        this.details = details;
    }
}
