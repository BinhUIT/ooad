package com.example.ooad.service.invoice.implementation;

import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.service.invoice.interfaces.InvoiceService;
import com.example.ooad.utils.Message;

@Service
public class InvoiceServiceImplementation implements InvoiceService {
    private final InvoiceRepository invoiceRepo;
    public InvoiceServiceImplementation(InvoiceRepository invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }
    @Override
    public Invoice findInvoiceById(int invoiceId) {
        return invoiceRepo.findById(invoiceId).orElseThrow(()->new NotFoundException(Message.invoiceNotFound));
    }
    
}
