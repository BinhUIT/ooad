package com.example.ooad.domain.entity;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="medicine_import")
public class MedicineImport {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int importId;
    private Date importDate;
    @ManyToOne
    @JoinColumn(name="importer_id")
    private Staff importer;
    @Column(name="supplier", columnDefinition="VARCHAR(100)")
    private String supplier;
    private int totalQuantity;
    @Column(precision=18,scale=2)
    private BigDecimal totalValue=new BigDecimal(0);
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
    public Staff getImporter() {
        return importer;
    }
    public void setImporter(Staff importer) {
        this.importer = importer;
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
    public MedicineImport() {
    }
    public MedicineImport(int importId, Date importDate, Staff importer, String supplier, int totalQuantity,
            BigDecimal totalValue) {
        this.importId = importId;
        this.importDate = importDate;
        this.importer = importer;
        this.supplier = supplier;
        this.totalQuantity = totalQuantity;
        this.totalValue = totalValue;
    }
    
    
}
