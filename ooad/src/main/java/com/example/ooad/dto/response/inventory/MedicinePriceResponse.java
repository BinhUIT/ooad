package com.example.ooad.dto.response.inventory;

import java.math.BigDecimal;
import java.sql.Date;

public class MedicinePriceResponse {
    private int medicineId;
    private Date effectiveDate;
    private BigDecimal unitPrice;
    private boolean canDelete; // can only delete if added today

    public MedicinePriceResponse() {
    }

    public MedicinePriceResponse(int medicineId, Date effectiveDate, BigDecimal unitPrice, boolean canDelete) {
        this.medicineId = medicineId;
        this.effectiveDate = effectiveDate;
        this.unitPrice = unitPrice;
        this.canDelete = canDelete;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
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

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}
