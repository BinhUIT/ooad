package com.example.ooad.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="invoice_medicine_detail")
public class InvoiceMedicineDetail extends InvoiceDetail {
    
    
    @ManyToOne
    @JoinColumn(name="medicine_id")
    private Medicine medicine;
    
    
    public Medicine getMedicine() {
        return medicine;
    }
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
    
    
    public InvoiceMedicineDetail() {
        super();
    }
    public InvoiceMedicineDetail(int detailId, Invoice invoice, Medicine medicine, int quantity, BigDecimal salePrice,
            BigDecimal amount) {
        super(detailId,invoice,quantity,salePrice,amount);
        this.medicine=medicine;
    }
    
}
