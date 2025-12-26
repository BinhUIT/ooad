package com.example.ooad.dto.request.invoice;

public class UpdateInvoiceRequest {
    private Integer paymentMethodId;
    
    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}
