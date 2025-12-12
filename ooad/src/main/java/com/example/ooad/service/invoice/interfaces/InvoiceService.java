package com.example.ooad.service.invoice.interfaces;

import com.example.ooad.domain.entity.Invoice;

public interface InvoiceService {
    public Invoice findInvoiceById(int invoiceId);
}
