package com.example.ooad.controller.patient;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.invoice.InvoiceDetailResponse;
import com.example.ooad.dto.response.invoice.InvoiceListResponse;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.PatientRepository;
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
    private final AccountRepository accountRepository;
    private final PatientRepository patientRepository;
    
    public PatientInvoiceController(
            InvoiceService invoiceService,
            AccountRepository accountRepository,
            PatientRepository patientRepository) {
        this.invoiceService = invoiceService;
        this.accountRepository = accountRepository;
        this.patientRepository = patientRepository;
    }
    
    /**
     * Get patient's invoice history
     * Patient identity is derived from JWT (anti-IDOR).
     */
    @GetMapping("/invoices")
    public ResponseEntity<GlobalResponse<Page<InvoiceListResponse>>> getMyInvoices(
            @AuthenticationPrincipal UserDetails currentUser,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int page,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int size) {
        
        int patientId = getCurrentPatientId(currentUser);
        Page<InvoiceListResponse> result = invoiceService.getPatientInvoices(patientId, page, size);
        GlobalResponse<Page<InvoiceListResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get patient's invoice detail
     * @param invoiceId The invoice ID to view
     * Patient identity is derived from JWT (anti-IDOR).
     */
    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<GlobalResponse<InvoiceDetailResponse>> getMyInvoiceDetail(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable int invoiceId) {
        
        int patientId = getCurrentPatientId(currentUser);
        InvoiceDetailResponse result = invoiceService.getPatientInvoiceDetail(patientId, invoiceId);
        GlobalResponse<InvoiceDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private int getCurrentPatientId(UserDetails currentUser) {
        if (currentUser == null) {
            throw new NotFoundException("Unauthenticated user");
        }

        String username = currentUser.getUsername();
        var account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }

        return patientRepository
                .findByAccountId(account.getAccountId())
                .orElseThrow(() -> new NotFoundException("Patient not found"))
                .getPatientId();
    }
}
