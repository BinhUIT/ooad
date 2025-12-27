package com.example.ooad.dto.request.invoice;

import java.util.List;

public class UpdateInvoiceMedicineDetailsRequest {
    private List<InvoiceMedicineDetailRequest> details;

    public List<InvoiceMedicineDetailRequest> getDetails() {
        return details;
    }

    public void setDetails(List<InvoiceMedicineDetailRequest> details) {
        this.details = details;
    }
}
