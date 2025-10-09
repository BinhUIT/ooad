package com.example.ooad.domain.entity;

import java.math.BigDecimal;
import java.sql.Date;

import com.example.ooad.domain.compositekey.MedicineInventoryKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name="medicine_inventory")
public class MedicineInventory {
    @EmbeddedId
    private MedicineInventoryKey medicineInventoryId;
    private int quantityInStock;
    private Date expiryDate;
    @Column(precision=18, scale=2)
    private BigDecimal importPrice;
    @ManyToOne
    @MapsId("importId")
    @JoinColumn(name="import_id")
    private MedicineImport medicineImport;
    @ManyToOne
    @MapsId("medicineId")
    @JoinColumn(name="medicine_id")
    private Medicine medicine;
    public MedicineInventoryKey getMedicineInventoryId() {
        return medicineInventoryId;
    }
    public void setMedicineInventoryId(MedicineInventoryKey medicineInventoryId) {
        this.medicineInventoryId = medicineInventoryId;
    }
    public int getQuantityInStock() {
        return quantityInStock;
    }
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public BigDecimal getImportPrice() {
        return importPrice;
    }
    public void setImportPrice(BigDecimal importPrice) {
        this.importPrice = importPrice;
    }
    public MedicineImport getMedicineImport() {
        return medicineImport;
    }
    public void setMedicineImport(MedicineImport medicineImport) {
        this.medicineImport = medicineImport;
    }
    public Medicine getMedicine() {
        return medicine;
    }
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
    public MedicineInventory() {
    }
    public MedicineInventory(MedicineInventoryKey medicineInventoryId, int quantityInStock, Date expiryDate,
            BigDecimal importPrice, MedicineImport medicineImport, Medicine medicine) {
        this.medicineInventoryId = medicineInventoryId;
        this.quantityInStock = quantityInStock;
        this.expiryDate = expiryDate;
        this.importPrice = importPrice;
        this.medicineImport = medicineImport;
        this.medicine = medicine;
    }
    
}
