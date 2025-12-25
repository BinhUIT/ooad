package com.example.ooad.service.payment.interfaces;

import java.util.Map;

import com.example.ooad.dto.request.payment.CreatePaymentRequest;
import com.example.ooad.dto.response.payment.PaymentInfoResponse;
import com.example.ooad.dto.response.payment.PaymentLinkResponse;

public interface PayOSService {
    
    /**
     * Create a PayOS payment link for an invoice
     */
    PaymentLinkResponse createPaymentLink(CreatePaymentRequest request);
    
    /**
     * Get payment information by order code
     */
    PaymentInfoResponse getPaymentInfo(Long orderCode);
    
    /**
     * Cancel a payment link
     */
    PaymentInfoResponse cancelPaymentLink(Long orderCode, String cancellationReason);
    
    /**
     * Verify payment and update invoice status
     */
    PaymentInfoResponse verifyAndUpdatePayment(int invoiceId, Long orderCode);
    
    /**
     * Handle webhook from PayOS
     */
    void handleWebhook(Map<String, Object> webhookData);
}
