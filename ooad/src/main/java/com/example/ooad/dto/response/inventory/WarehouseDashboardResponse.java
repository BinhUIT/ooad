package com.example.ooad.dto.response.inventory;

import java.math.BigDecimal;
import java.util.List;

public class WarehouseDashboardResponse {
    private long totalMedicines;
    private long totalOutOfStock;
    private long totalExpiringSoon;
    private BigDecimal totalImportValueMonth;
    private List<MedicineImportListResponse> recentImports;

    public WarehouseDashboardResponse() {
    }

    public long getTotalMedicines() {
        return totalMedicines;
    }

    public void setTotalMedicines(long totalMedicines) {
        this.totalMedicines = totalMedicines;
    }

    public long getTotalOutOfStock() {
        return totalOutOfStock;
    }

    public void setTotalOutOfStock(long totalOutOfStock) {
        this.totalOutOfStock = totalOutOfStock;
    }

    public long getTotalExpiringSoon() {
        return totalExpiringSoon;
    }

    public void setTotalExpiringSoon(long totalExpiringSoon) {
        this.totalExpiringSoon = totalExpiringSoon;
    }

    public BigDecimal getTotalImportValueMonth() {
        return totalImportValueMonth;
    }

    public void setTotalImportValueMonth(BigDecimal totalImportValueMonth) {
        this.totalImportValueMonth = totalImportValueMonth;
    }

    public List<MedicineImportListResponse> getRecentImports() {
        return recentImports;
    }

    public void setRecentImports(List<MedicineImportListResponse> recentImports) {
        this.recentImports = recentImports;
    }
}
