package com.example.ooad.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int detailId;
    @ManyToOne
    @JoinColumn(name="invoice_id")
    protected Invoice invoice;
    @Column(name="quantity", nullable=false)
    protected int quantity;
    @Column(precision=18, scale=2, nullable=false)
    protected BigDecimal salePrice;
    @Column(precision=18, scale=2)
    protected BigDecimal amount;
    public InvoiceDetail() {
    }
    public InvoiceDetail(int detailId, Invoice invoice, int quantity, BigDecimal salePrice, BigDecimal amount) {
        this.detailId = detailId;
        this.invoice = invoice;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.amount = amount;
    }
    public int getDetailId() {
        return detailId;
    }
    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }
    public Invoice getInvoice() {
        return invoice;
    }
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getSalePrice() {
        return salePrice;
    }
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
}
