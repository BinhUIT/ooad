package com.example.ooad.controller.invoice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.invoice.interfaces.InvoiceService;
import com.example.ooad.utils.Message;

@RestController
public class InvoiceController {
    private final InvoiceService invoiceService;
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    } 
    @GetMapping({"/receptionist/invoice_by_id/{invoiceId}","/admin/invoice_by_id/{invoiceId}"}) 
    public ResponseEntity<GlobalResponse<Invoice>> getInvoiceById(@PathVariable int invoiceId) {
        Invoice result = invoiceService.findInvoiceById(invoiceId);
        GlobalResponse<Invoice> response = new GlobalResponse<>(result,Message.success , 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
