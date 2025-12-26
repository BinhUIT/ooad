package com.example.ooad.dto.request.invoice;

import java.util.List;

public class UpdateInvoiceServiceDetailsRequest {
    private List<InvoiceServiceDetailRequest> details;

    public List<InvoiceServiceDetailRequest> getDetails() {
        return details;
    }

    public void setDetails(List<InvoiceServiceDetailRequest> details) {
        this.details = details;
    }
}
