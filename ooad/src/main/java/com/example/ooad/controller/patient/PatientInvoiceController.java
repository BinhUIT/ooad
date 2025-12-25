package com.example.ooad.controller.patient;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.invoice.InvoiceDetailResponse;
import com.example.ooad.dto.response.invoice.InvoiceListResponse;
import com.example.ooad.service.invoice.interfaces.InvoiceService;
import com.example.ooad.utils.Message;

/**
 * Controller for patient-specific invoice endpoints.
 * Patients can only view their own invoices.
 */
@RestController
@RequestMapping("/patient")
public class PatientInvoiceController {
    
    private final InvoiceService invoiceService;
    
    public PatientInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    
    /**
     * Get patient's invoice history
     * @param patientId The patient ID (should be validated from JWT token in production)
     */
    @GetMapping("/invoices")
    public ResponseEntity<GlobalResponse<Page<InvoiceListResponse>>> getMyInvoices(
            @RequestParam int patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<InvoiceListResponse> result = invoiceService.getPatientInvoices(patientId, page, size);
        GlobalResponse<Page<InvoiceListResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get patient's invoice detail
     * @param patientId The patient ID (should be validated from JWT token in production)
     * @param invoiceId The invoice ID to view
     */
    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<GlobalResponse<InvoiceDetailResponse>> getMyInvoiceDetail(
            @RequestParam int patientId,
            @PathVariable int invoiceId) {
        
        InvoiceDetailResponse result = invoiceService.getPatientInvoiceDetail(patientId, invoiceId);
        GlobalResponse<InvoiceDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
