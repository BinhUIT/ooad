package com.example.ooad.domain.entity;

import java.sql.Date;

import com.example.ooad.domain.compositekey.ImportDetailKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name="import_detail")
public class ImportDetail {
   @EmbeddedId
   private ImportDetailKey importDetailId;
   @ManyToOne
   @MapsId("importId")
   @JoinColumn(name="import_id")
   private MedicineImport medicineImport;
   @ManyToOne
   @MapsId("medicineId")
   @JoinColumn(name="medicine_id")
   private Medicine medicine;
   private int quantity;
   private Date expiryDate;
   @Column(name="import_price", columnDefinition="DECIMAL(18,2)")
   private float importPrice;
   public ImportDetail(ImportDetailKey importDetailId, MedicineImport medicineImport, Medicine medicine, int quantity,
        Date expiryDate, float importPrice) {
    this.importDetailId = importDetailId;
    this.medicineImport = medicineImport;
    this.medicine = medicine;
    this.quantity = quantity;
    this.expiryDate = expiryDate;
    this.importPrice = importPrice;
   }
   public ImportDetail() {
   }
   public ImportDetailKey getImportDetailId() {
    return importDetailId;
   }
   public void setImportDetailId(ImportDetailKey importDetailId) {
    this.importDetailId = importDetailId;
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
   public int getQuantity() {
    return quantity;
   }
   public void setQuantity(int quantity) {
    this.quantity = quantity;
   }
   public Date getExpiryDate() {
    return expiryDate;
   }
   public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
   }
   public float getImportPrice() {
    return importPrice;
   }
   public void setImportPrice(float importPrice) {
    this.importPrice = importPrice;
   }
   


}
