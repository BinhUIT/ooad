package com.example.ooad.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="invoice_service_detail")
public class InvoiceServiceDetail extends InvoiceDetail {
    @ManyToOne
    @JoinColumn(name="service_id")
    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
    public InvoiceServiceDetail() {
        super();
    }

    public InvoiceServiceDetail(int detailId, Invoice invoice, int quantity, BigDecimal salePrice, BigDecimal amount,
            Service service) {
        super(detailId, invoice, quantity, salePrice, amount);
        this.service = service;
    } 
    
}
