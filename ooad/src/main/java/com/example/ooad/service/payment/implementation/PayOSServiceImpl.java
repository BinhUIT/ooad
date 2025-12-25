package com.example.ooad.service.payment.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.enums.EPaymentStatus;
import com.example.ooad.dto.request.payment.CreatePaymentRequest;
import com.example.ooad.dto.response.payment.PaymentInfoResponse;
import com.example.ooad.dto.response.payment.PaymentLinkResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.RefPaymentMethodRepository;
import com.example.ooad.service.payment.interfaces.PayOSService;
import com.example.ooad.service.payment.internal.PayOSCheckoutResponse;
import com.example.ooad.service.payment.internal.PayOSClient;
import com.example.ooad.service.payment.internal.PayOSCreateRequest;
import com.example.ooad.service.payment.internal.PayOSPaymentLinkResponse;

@Service
public class PayOSServiceImpl implements PayOSService {

    private static final Logger log = LoggerFactory.getLogger(PayOSServiceImpl.class);
    private static final String DEFAULT_CURRENCY = "VND";

    private final InvoiceRepository invoiceRepository;
    private final RefPaymentMethodRepository paymentMethodRepository;
    private final PayOSClient payOSClient;

    @Value("${payos.return-url:http://localhost:5173/payment/success}")
    private String returnUrl;

    @Value("${payos.cancel-url:http://localhost:5173/payment/cancel}")
    private String cancelUrl;

    public PayOSServiceImpl(InvoiceRepository invoiceRepository,
            RefPaymentMethodRepository paymentMethodRepository,
            PayOSClient payOSClient) {
        this.invoiceRepository = invoiceRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.payOSClient = payOSClient;
    }

    private void ensurePayOSClientConfigured() {
        if (payOSClient == null) {
            throw new BadRequestException("PayOS client is not configured");
        }
    }

    @Override
    @Transactional
    public PaymentLinkResponse createPaymentLink(CreatePaymentRequest request) {
        ensurePayOSClientConfigured();

        try {
            Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                    .orElseThrow(() -> new NotFoundException("Invoice not found"));

            if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
                throw new BadRequestException("Invoice is already paid");
            }

            BigDecimal amount = request.getAmount();
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                amount = invoice.getTotalAmount();
            }
            int amountInt = amount.intValue();
            long orderCode = System.currentTimeMillis();

            String invoiceCode = buildInvoiceCode(invoice);
            String description = buildDescription(request.getDescription(), invoiceCode);

            PayOSCreateRequest.Item item = PayOSCreateRequest.Item.builder()
                    .name("Invoice " + invoiceCode)
                    .quantity(1)
                    .price(amountInt)
                    .build();

            PayOSCreateRequest payOSRequest = PayOSCreateRequest.builder()
                    .orderCode(orderCode)
                    .amount(amountInt)
                    .currency(DEFAULT_CURRENCY)
                    .description(description)
                    .returnUrl(returnUrl + "?invoiceId=" + invoice.getInvoiceId())
                    .cancelUrl(cancelUrl + "?invoiceId=" + invoice.getInvoiceId())
                    .addItem(item)
                    .build();

            PayOSCheckoutResponse response = payOSClient.createPaymentLink(payOSRequest);
            log.info("Payment link created successfully for invoice: {}, orderCode: {}",
                    invoice.getInvoiceId(), orderCode);

            return toPaymentLinkResponse(response, description, orderCode);
        } catch (NotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating payment link: ", e);
            throw new RuntimeException("Failed to create payment link: " + e.getMessage());
        }
    }

    @Override
    public PaymentInfoResponse getPaymentInfo(Long orderCode) {
        ensurePayOSClientConfigured();

        try {
            PayOSPaymentLinkResponse paymentLink = payOSClient.getPaymentLink(orderCode);
            return toPaymentInfoResponse(paymentLink);
        } catch (Exception e) {
            log.error("Error getting payment info: ", e);
            throw new RuntimeException("Failed to get payment info: " + e.getMessage());
        }
    }

    @Override
    public PaymentInfoResponse cancelPaymentLink(Long orderCode, String cancellationReason) {
        ensurePayOSClientConfigured();

        try {
            PayOSPaymentLinkResponse response = payOSClient.cancelPaymentLink(orderCode,
                    cancellationReason != null ? cancellationReason : "User cancelled");
            log.info("Payment link cancelled for orderCode: {}", orderCode);
            return toPaymentInfoResponse(response);
        } catch (Exception e) {
            log.error("Error cancelling payment link: ", e);
            throw new RuntimeException("Failed to cancel payment link: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentInfoResponse verifyAndUpdatePayment(int invoiceId, Long orderCode) {
        ensurePayOSClientConfigured();

        try {
            PaymentInfoResponse paymentInfo = getPaymentInfo(orderCode);

            if (paymentInfo != null && "PAID".equals(paymentInfo.getStatus())) {
                Invoice invoice = invoiceRepository.findById(invoiceId)
                        .orElseThrow(() -> new NotFoundException("Invoice not found"));

                if (invoice.getPaymentStatus() != EPaymentStatus.PAID) {
                    invoice.setPaymentStatus(EPaymentStatus.PAID);
                    invoice.setInvoiceDate(Date.valueOf(LocalDate.now()));

                    if (invoice.getPaymentMethod() == null) {
                        paymentMethodRepository.findByMethodCode("BANK_TRANSFER")
                                .ifPresent(invoice::setPaymentMethod);
                    }

                    invoiceRepository.save(invoice);
                    log.info("Invoice status updated to PAID for invoice: {}", invoiceId);
                }
            }

            return paymentInfo;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error verifying payment: ", e);
            throw new RuntimeException("Failed to verify payment: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handleWebhook(Map<String, Object> webhookData) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) webhookData.get("data");

            if (data == null) {
                log.warn("Webhook data is null");
                return;
            }

            Object orderCodeObj = data.get("orderCode");
            Long orderCode = null;

            if (orderCodeObj instanceof Integer) {
                orderCode = ((Integer) orderCodeObj).longValue();
            } else if (orderCodeObj instanceof Long) {
                orderCode = (Long) orderCodeObj;
            } else if (orderCodeObj instanceof String) {
                orderCode = Long.parseLong((String) orderCodeObj);
            }

            if (orderCode == null) {
                log.warn("Order code is null in webhook");
                return;
            }

            String code = (String) webhookData.get("code");

            if ("00".equals(code)) {
                log.info("Payment webhook received for orderCode: {}", orderCode);

                Object descObj = data.get("description");
                if (descObj != null) {
                    String desc = descObj.toString();
                    if (desc.startsWith("HD")) {
                        try {
                            int invoiceId = Integer.parseInt(desc.substring(2).trim());
                            verifyAndUpdatePayment(invoiceId, orderCode);
                        } catch (NumberFormatException e) {
                            log.warn("Could not parse invoice ID from description: {}", desc);
                        }
                    }
                }

                log.info("Payment successful for order: {}, amount: {}",
                        orderCode, data.get("amount"));
            }

        } catch (Exception e) {
            log.error("Error handling webhook: ", e);
        }
    }

    private PaymentLinkResponse toPaymentLinkResponse(PayOSCheckoutResponse response, String description,
            long orderCode) {
        if (response == null) {
            return null;
        }
        return PaymentLinkResponse.builder()
                .bin(response.getBin())
                .accountNumber(response.getAccountNumber())
                .accountName(response.getAccountName())
                .amount(response.getAmount())
                .description(description)
                .orderCode(orderCode)
                .currency(response.getCurrency())
                .paymentLinkId(response.getPaymentLinkId())
                .status(response.getStatus())
                .checkoutUrl(response.getCheckoutUrl())
                .qrCode(response.getQrCode())
                .build();
    }

    private PaymentInfoResponse toPaymentInfoResponse(PayOSPaymentLinkResponse paymentLink) {
        if (paymentLink == null) {
            return null;
        }
        return PaymentInfoResponse.builder()
                .id(paymentLink.getId())
                .orderCode(paymentLink.getOrderCode())
                .amount(paymentLink.getAmount())
                .amountPaid(paymentLink.getAmountPaid())
                .amountRemaining(paymentLink.getAmountRemaining())
                .status(paymentLink.getStatus())
                .createdAt(paymentLink.getCreatedAt())
                .canceledAt(paymentLink.getCanceledAt())
                .cancellationReason(paymentLink.getCancellationReason())
                .build();
    }

    private String buildInvoiceCode(Invoice invoice) {
        return String.valueOf(invoice.getInvoiceId());
    }

    private String buildDescription(String requested, String invoiceCode) {
        // Use simple ASCII characters only - no Vietnamese diacritics
        // Keep it short (under 25 characters) for VietQR compatibility
        String description;
        if (requested != null && !requested.isEmpty()) {
            // Remove Vietnamese diacritics and special characters
            description = removeVietnameseDiacritics(requested);
        } else {
            description = "HD" + invoiceCode;
        }
        // Ensure max 25 characters and only alphanumeric + space
        description = description.replaceAll("[^a-zA-Z0-9 ]", "").trim();
        if (description.length() > 25) {
            description = description.substring(0, 25);
        }
        if (description.isEmpty()) {
            description = "HD" + invoiceCode;
        }
        return description;
    }
    
    private String removeVietnameseDiacritics(String str) {
        String result = str;
        // Vietnamese vowels with diacritics
        result = result.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        result = result.replaceAll("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]", "A");
        result = result.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        result = result.replaceAll("[ÈÉẸẺẼÊỀẾỆỂỄ]", "E");
        result = result.replaceAll("[ìíịỉĩ]", "i");
        result = result.replaceAll("[ÌÍỊỈĨ]", "I");
        result = result.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        result = result.replaceAll("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]", "O");
        result = result.replaceAll("[ùúụủũưừứựửữ]", "u");
        result = result.replaceAll("[ÙÚỤỦŨƯỪỨỰỬỮ]", "U");
        result = result.replaceAll("[ỳýỵỷỹ]", "y");
        result = result.replaceAll("[ỲÝỴỶỸ]", "Y");
        result = result.replaceAll("[đ]", "d");
        result = result.replaceAll("[Đ]", "D");
        return result;
    }
}
