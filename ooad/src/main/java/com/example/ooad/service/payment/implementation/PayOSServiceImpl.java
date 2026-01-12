package com.example.ooad.service.payment.implementation;

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

import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLink;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
// -----------------------------------------------------

@Service
public class PayOSServiceImpl implements PayOSService {

    private static final Logger log = LoggerFactory.getLogger(PayOSServiceImpl.class);
    private static final String DEFAULT_CURRENCY = "VND";

    private final InvoiceRepository invoiceRepository;
    private final RefPaymentMethodRepository paymentMethodRepository;

    // Cấu hình PayOS trực tiếp
    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Value("${payos.return-url}")
    private String returnUrl;

    @Value("${payos.cancel-url}")
    private String cancelUrl;

    // Khởi tạo PayOS SDK
    private PayOS getPayOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }

    public PayOSServiceImpl(InvoiceRepository invoiceRepository,
            RefPaymentMethodRepository paymentMethodRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    @Transactional
    public PaymentLinkResponse createPaymentLink(CreatePaymentRequest request) {
        try {
            Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                    .orElseThrow(() -> new NotFoundException("Invoice not found"));

            if (invoice.getPaymentStatus() == EPaymentStatus.PAID) {
                throw new BadRequestException("Invoice is already paid");
            }

            // Tính toán số tiền (Dùng int cho chuẩn SDK PayOS v2.0.1)
            int amount;
            if (request.getAmount() == null || request.getAmount().intValue() <= 0) {
                amount = invoice.getTotalAmount().intValue();
            } else {
                amount = request.getAmount().intValue();
            }
            
            // DEMO MODE: Giảm số tiền xuống dưới 100,000 VND cho mục đích test
            // PayOS sandbox có giới hạn số tiền thanh toán
            int originalAmount = amount;
            if (amount >= 100000) {
                if (amount >= 1000000) {
                    amount = amount / 1000; // Chia cho 1000 nếu >= 1 triệu
                } else if (amount >= 100000) {
                    amount = amount / 100;  // Chia cho 100 nếu >= 100k
                }
            }
            // Đảm bảo amount tối thiểu là 2000 VND (yêu cầu của PayOS)
            if (amount < 2000) {
                amount = 2000;
            }
            log.info("DEMO MODE: Original amount: {} VND -> Demo amount: {} VND", originalAmount, amount);
            
            long orderCode = System.currentTimeMillis();
            String invoiceCode = String.valueOf(invoice.getInvoiceId());
            
            // Xây dựng description chuẩn: "HD" + mã (Không dấu cách, không ký tự lạ)
            String description = "HD" + invoiceCode;

            // Tạo Item data cho PayOS
            PaymentLinkItem item = PaymentLinkItem.builder()
                    .name("ThanhToanDonHang")
                    .quantity(1)
                    .price((long) amount)
                    .build();

            // Build Request sử dụng SDK PayOS
            CreatePaymentLinkRequest paymentRequest = CreatePaymentLinkRequest.builder()
                    .orderCode(orderCode)
                    .amount((long) amount)
                    .description(description)
                    .returnUrl(returnUrl + "?invoiceId=" + invoice.getInvoiceId())
                    .cancelUrl(cancelUrl + "?invoiceId=" + invoice.getInvoiceId())
                    .item(item)
                    .build();

            // Gọi API tạo link
            PayOS payOS = getPayOS();
            CreatePaymentLinkResponse response = payOS.paymentRequests().create(paymentRequest);
            
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
        try {
            PayOS payOS = getPayOS();
            // Gọi SDK để lấy thông tin thanh toán
            PaymentLink paymentLink = payOS.paymentRequests().get(orderCode);
            
            // Map từ object SDK sang DTO của project
            return toPaymentInfoResponse(paymentLink);
        } catch (Exception e) {
            log.error("Error getting payment info: ", e);
            throw new RuntimeException("Failed to get payment info: " + e.getMessage());
        }
    }

    @Override
    public PaymentInfoResponse cancelPaymentLink(Long orderCode, String cancellationReason) {
        try {
            PayOS payOS = getPayOS();
            PaymentLink response = payOS.paymentRequests().cancel(orderCode,
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
        try {
            // Lấy thông tin mới nhất từ PayOS
            PaymentInfoResponse paymentInfo = getPaymentInfo(orderCode);

            if (paymentInfo != null && "PAID".equals(paymentInfo.getStatus())) {
                Invoice invoice = invoiceRepository.findById(invoiceId)
                        .orElseThrow(() -> new NotFoundException("Invoice not found"));

                if (invoice.getPaymentStatus() != EPaymentStatus.PAID) {
                    invoice.setPaymentStatus(EPaymentStatus.PAID);
                    invoice.setInvoiceDate(Date.valueOf(LocalDate.now()));

                    // Set default method nếu chưa có
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
                    // Parse "HD41" -> 41
                    if (desc.startsWith("HD")) {
                        try {
                            // Cắt bỏ chữ HD và lấy phần số
                            String idPart = desc.substring(2).trim();
                            // Đảm bảo chỉ lấy số (phòng trường hợp ngân hàng nối thêm nội dung)
                            idPart = idPart.replaceAll("[^0-9]", "");
                            
                            if (!idPart.isEmpty()) {
                                int invoiceId = Integer.parseInt(idPart);
                                verifyAndUpdatePayment(invoiceId, orderCode);
                            }
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

    // --- Helper Mappers: Map từ SDK Object sang DTO ---

    private PaymentLinkResponse toPaymentLinkResponse(CreatePaymentLinkResponse response, String description,
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
                .currency(DEFAULT_CURRENCY)
                .paymentLinkId(response.getPaymentLinkId())
                .status(response.getStatus() != null ? response.getStatus().name() : null)
                .checkoutUrl(response.getCheckoutUrl())
                .qrCode(response.getQrCode())
                .build();
    }

    private PaymentInfoResponse toPaymentInfoResponse(PaymentLink paymentLink) {
        if (paymentLink == null) {
            return null;
        }
        
        return PaymentInfoResponse.builder()
                .id(paymentLink.getId())
                .orderCode(paymentLink.getOrderCode())
                .amount(paymentLink.getAmount())
                .amountPaid(paymentLink.getAmountPaid())
                .amountRemaining(paymentLink.getAmountRemaining())
                .status(paymentLink.getStatus() != null ? paymentLink.getStatus().name() : null)
                .createdAt(paymentLink.getCreatedAt())
                .canceledAt(paymentLink.getCanceledAt())
                .cancellationReason(paymentLink.getCancellationReason())
                .build();
    }
}
