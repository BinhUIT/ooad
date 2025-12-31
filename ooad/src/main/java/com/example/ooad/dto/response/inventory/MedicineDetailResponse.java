package com.example.ooad.dto.response.inventory;

import java.math.BigDecimal;
import java.util.List;

public class MedicineDetailResponse {
    private MedicineResponse medicine;
    private BigDecimal currentPrice;
    private List<MedicineInventoryResponse> inventories; // danh sách tồn kho (có thể ẩn số lượng = 0)
    private List<MedicinePriceResponse> priceHistory; // lịch sử giá bán

    public MedicineDetailResponse() {
    }

    public MedicineResponse getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineResponse medicine) {
        this.medicine = medicine;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public List<MedicineInventoryResponse> getInventories() {
        return inventories;
    }

    public void setInventories(List<MedicineInventoryResponse> inventories) {
        this.inventories = inventories;
    }

    public List<MedicinePriceResponse> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<MedicinePriceResponse> priceHistory) {
        this.priceHistory = priceHistory;
    }
}
