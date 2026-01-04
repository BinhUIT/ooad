package com.example.ooad.dto.response.inventory;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class MedicineImportDetailResponse {
    private int importId;
    private Date importDate;
    private int importerId;
    private String importerName;
    private String supplier;
    private int totalQuantity;
    private BigDecimal totalValue;
    private List<ImportDetailResponse> details;

    public MedicineImportDetailResponse() {
    }

    public int getImportId() {
        return importId;
    }

    public void setImportId(int importId) {
        this.importId = importId;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public int getImporterId() {
        return importerId;
    }

    public void setImporterId(int importerId) {
        this.importerId = importerId;
    }

    public String getImporterName() {
        return importerName;
    }

    public void setImporterName(String importerName) {
        this.importerName = importerName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public List<ImportDetailResponse> getDetails() {
        return details;
    }

    public void setDetails(List<ImportDetailResponse> details) {
        this.details = details;
    }
}
