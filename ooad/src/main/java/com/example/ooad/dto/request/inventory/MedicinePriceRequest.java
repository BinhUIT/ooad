package com.example.ooad.dto.request.inventory;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class MedicinePriceRequest {

    @NotNull(message = "Effective date is required")
    private Date effectiveDate;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;

    public MedicinePriceRequest() {
    }

    public MedicinePriceRequest(Date effectiveDate, BigDecimal unitPrice) {
        this.effectiveDate = effectiveDate;
        this.unitPrice = unitPrice;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
