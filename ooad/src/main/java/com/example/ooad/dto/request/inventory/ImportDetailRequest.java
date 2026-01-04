package com.example.ooad.dto.request.inventory;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ImportDetailRequest {
    
    @NotNull(message = "Medicine ID is required")
    private Integer medicineId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @NotNull(message = "Import price is required")
    @Min(value = 0, message = "Import price must be non-negative")
    private BigDecimal importPrice;
    
    @NotNull(message = "Expiry date is required")
    private Date expiryDate;

    public ImportDetailRequest() {
    }

    public ImportDetailRequest(Integer medicineId, Integer quantity, BigDecimal importPrice, Date expiryDate) {
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.importPrice = importPrice;
        this.expiryDate = expiryDate;
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(BigDecimal importPrice) {
        this.importPrice = importPrice;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
