package com.example.ooad.dto.request.payment;

public class VerifyPaymentRequest {
    private int invoiceId;
    private Long orderCode;

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }
}
