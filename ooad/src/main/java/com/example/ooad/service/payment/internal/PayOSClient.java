package com.example.ooad.service.payment.internal;

public interface PayOSClient {
    PayOSCheckoutResponse createPaymentLink(PayOSCreateRequest request);

    PayOSPaymentLinkResponse getPaymentLink(Long orderCode);

    PayOSPaymentLinkResponse cancelPaymentLink(Long orderCode, String cancellationReason);
}
