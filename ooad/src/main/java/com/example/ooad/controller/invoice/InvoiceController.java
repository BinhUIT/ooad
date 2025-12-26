package com.example.ooad.controller.invoice;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.enums.EPaymentStatus;
import com.example.ooad.dto.request.invoice.InvoiceMedicineDetailRequest;
import com.example.ooad.dto.request.invoice.InvoiceSearchRequest;
import com.example.ooad.dto.request.invoice.InvoiceServiceDetailRequest;
import com.example.ooad.dto.request.invoice.UpdateInvoiceMedicineDetailsRequest;
import com.example.ooad.dto.request.invoice.UpdateInvoiceRequest;
import com.example.ooad.dto.request.invoice.UpdateInvoiceServiceDetailsRequest;
import com.example.ooad.dto.request.payment.CreatePaymentRequest;
import com.example.ooad.dto.request.payment.VerifyPaymentRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.invoice.AvailableMedicineResponse;
import com.example.ooad.dto.response.invoice.InvoiceDetailResponse;
import com.example.ooad.dto.response.invoice.InvoiceListResponse;
import com.example.ooad.dto.response.payment.PaymentInfoResponse;
import com.example.ooad.dto.response.payment.PaymentLinkResponse;
import com.example.ooad.service.invoice.interfaces.InvoiceService;
import com.example.ooad.service.payment.interfaces.PayOSService;
import com.example.ooad.utils.Message;

@RestController
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final PayOSService payOSService;
    
    public InvoiceController(InvoiceService invoiceService, PayOSService payOSService) {
        this.invoiceService = invoiceService;
        this.payOSService = payOSService;
    }
    
    // ==================== STAFF ENDPOINTS ====================
    
    /**
     * Search invoices with filters (for receptionist/admin)
     */
    @GetMapping({"/receptionist/invoices", "/admin/invoices"})
    public ResponseEntity<GlobalResponse<Page<InvoiceListResponse>>> searchInvoices(
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String patientPhone,
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer recordId,
            @RequestParam(required = false) EPaymentStatus paymentStatus,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        InvoiceSearchRequest request = new InvoiceSearchRequest();
        request.setPatientName(patientName);
        request.setPatientPhone(patientPhone);
        request.setPatientId(patientId);
        request.setRecordId(recordId);
        request.setPaymentStatus(paymentStatus);
        request.setFromDate(fromDate);
        request.setToDate(toDate);
        request.setPage(page);
        request.setSize(size);
        
        Page<InvoiceListResponse> result = invoiceService.searchInvoices(request);
        GlobalResponse<Page<InvoiceListResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get invoice by ID (legacy endpoint)
     */
    @GetMapping({"/receptionist/invoice_by_id/{invoiceId}", "/admin/invoice_by_id/{invoiceId}"})
    public ResponseEntity<GlobalResponse<Invoice>> getInvoiceById(@PathVariable int invoiceId) {
        Invoice result = invoiceService.findInvoiceById(invoiceId);
        GlobalResponse<Invoice> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get invoice detail with all tabs data
     */
    @GetMapping({"/receptionist/invoices/{invoiceId}", "/admin/invoices/{invoiceId}"})
    public ResponseEntity<GlobalResponse<InvoiceDetailResponse>> getInvoiceDetail(@PathVariable int invoiceId) {
        InvoiceDetailResponse result = invoiceService.getInvoiceDetail(invoiceId);
        GlobalResponse<InvoiceDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Update invoice (payment method)
     */
    @PutMapping({"/receptionist/invoices/{invoiceId}", "/admin/invoices/{invoiceId}"})
    public ResponseEntity<GlobalResponse<Invoice>> updateInvoice(
            @PathVariable int invoiceId,
            @RequestBody UpdateInvoiceRequest request) {
        Invoice result = invoiceService.updateInvoice(invoiceId, request);
        GlobalResponse<Invoice> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Update invoice medicine details (Tab 3: Hóa đơn thuốc)
     */
    @PutMapping({"/receptionist/invoices/{invoiceId}/medicine-details", "/admin/invoices/{invoiceId}/medicine-details"})
    public ResponseEntity<GlobalResponse<InvoiceDetailResponse>> updateMedicineDetails(
            @PathVariable int invoiceId,
            @RequestBody UpdateInvoiceMedicineDetailsRequest request) {
        InvoiceDetailResponse result = invoiceService.updateMedicineDetails(invoiceId, request.getDetails());
        GlobalResponse<InvoiceDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Add single medicine detail
     */
    @PostMapping({"/receptionist/invoices/{invoiceId}/medicine-details", "/admin/invoices/{invoiceId}/medicine-details"})
    public ResponseEntity<GlobalResponse<InvoiceDetailResponse>> addMedicineDetail(
            @PathVariable int invoiceId,
            @RequestBody InvoiceMedicineDetailRequest request) {
        InvoiceDetailResponse result = invoiceService.addMedicineDetail(invoiceId, request);
        GlobalResponse<InvoiceDetailResponse> response = new GlobalResponse<>(result, Message.success, 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Delete medicine detail
     */
    @DeleteMapping({"/receptionist/invoices/{invoiceId}/medicine-details/{detailId}", 
                   "/admin/invoices/{invoiceId}/medicine-details/{detailId}"})
    public ResponseEntity<GlobalResponse<Void>> deleteMedicineDetail(
            @PathVariable int invoiceId,
            @PathVariable int detailId) {
        invoiceService.deleteMedicineDetail(invoiceId, detailId);
        GlobalResponse<Void> response = new GlobalResponse<>(null, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Update invoice service details (Tab 2: Hóa đơn dịch vụ)
     */
    @PutMapping({"/receptionist/invoices/{invoiceId}/service-details", "/admin/invoices/{invoiceId}/service-details"})
    public ResponseEntity<GlobalResponse<InvoiceDetailResponse>> updateServiceDetails(
            @PathVariable int invoiceId,
            @RequestBody UpdateInvoiceServiceDetailsRequest request) {
        InvoiceDetailResponse result = invoiceService.updateServiceDetails(invoiceId, request.getDetails());
        GlobalResponse<InvoiceDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Add single service detail
     */
    @PostMapping({"/receptionist/invoices/{invoiceId}/service-details", "/admin/invoices/{invoiceId}/service-details"})
    public ResponseEntity<GlobalResponse<InvoiceDetailResponse>> addServiceDetail(
            @PathVariable int invoiceId,
            @RequestBody InvoiceServiceDetailRequest request) {
        InvoiceDetailResponse result = invoiceService.addServiceDetail(invoiceId, request);
        GlobalResponse<InvoiceDetailResponse> response = new GlobalResponse<>(result, Message.success, 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    /**
     * Delete service detail
     */
    @DeleteMapping({"/receptionist/invoices/{invoiceId}/service-details/{detailId}", 
                   "/admin/invoices/{invoiceId}/service-details/{detailId}"})
    public ResponseEntity<GlobalResponse<Void>> deleteServiceDetail(
            @PathVariable int invoiceId,
            @PathVariable int detailId) {
        invoiceService.deleteServiceDetail(invoiceId, detailId);
        GlobalResponse<Void> response = new GlobalResponse<>(null, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get available medicines for sale (with inventory check)
     */
    @GetMapping({"/receptionist/invoices/available-medicines", "/admin/invoices/available-medicines"})
    public ResponseEntity<GlobalResponse<List<AvailableMedicineResponse>>> getAvailableMedicines(
            @RequestParam(defaultValue = "3") int minMonthsBeforeExpiry) {
        List<AvailableMedicineResponse> result = invoiceService.getAvailableMedicines(minMonthsBeforeExpiry);
        GlobalResponse<List<AvailableMedicineResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Mark invoice as paid (cash payment)
     */
    @PostMapping({"/receptionist/invoices/{invoiceId}/mark-paid", "/admin/invoices/{invoiceId}/mark-paid"})
    public ResponseEntity<GlobalResponse<Invoice>> markAsPaid(
            @PathVariable int invoiceId,
            @RequestParam int paymentMethodId,
            @RequestParam int staffId) {
        Invoice result = invoiceService.markAsPaid(invoiceId, paymentMethodId, staffId);
        GlobalResponse<Invoice> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // ==================== PAYMENT (PayOS) ENDPOINTS ====================
    
    /**
     * Create PayOS payment link
     */
    @PostMapping({"/receptionist/payment/create-payment", "/admin/payment/create-payment", "/patient/payment/create-payment"})
    public ResponseEntity<GlobalResponse<PaymentLinkResponse>> createPaymentLink(@RequestBody CreatePaymentRequest request) {
        PaymentLinkResponse result = payOSService.createPaymentLink(request);
        GlobalResponse<PaymentLinkResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get payment info by order code
     */
    @GetMapping({"/receptionist/payment/payment-requests/{orderCode}", 
                "/admin/payment/payment-requests/{orderCode}",
                "/patient/payment/payment-requests/{orderCode}"})
    public ResponseEntity<GlobalResponse<PaymentInfoResponse>> getPaymentInfo(@PathVariable Long orderCode) {
        PaymentInfoResponse result = payOSService.getPaymentInfo(orderCode);
        GlobalResponse<PaymentInfoResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Cancel payment link
     */
    @PostMapping({"/receptionist/payment/payment-requests/{orderCode}/cancel",
                 "/admin/payment/payment-requests/{orderCode}/cancel",
                 "/patient/payment/payment-requests/{orderCode}/cancel"})
    public ResponseEntity<GlobalResponse<PaymentInfoResponse>> cancelPaymentLink(
            @PathVariable Long orderCode,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("cancellationReason") : null;
        PaymentInfoResponse result = payOSService.cancelPaymentLink(orderCode, reason);
        GlobalResponse<PaymentInfoResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Verify payment and update invoice
     */
    @PostMapping({"/receptionist/payment/verify-payment",
                 "/admin/payment/verify-payment",
                 "/patient/payment/verify-payment"})
    public ResponseEntity<GlobalResponse<PaymentInfoResponse>> verifyPayment(@RequestBody VerifyPaymentRequest request) {
        PaymentInfoResponse result = payOSService.verifyAndUpdatePayment(request.getInvoiceId(), request.getOrderCode());
        GlobalResponse<PaymentInfoResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * PayOS Webhook endpoint (public)
     */
    @PostMapping("/payment/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> webhookData) {
        payOSService.handleWebhook(webhookData);
        return ResponseEntity.ok("OK");
    }
}
