package com.example.ooad.service.payment.internal;

import java.util.Map;

public class PayOSPaymentLinkResponse {
    private String id;
    private Long orderCode;
    private Long amount;
    private Long amountPaid;
    private Long amountRemaining;
    private String status;
    private String createdAt;
    private String canceledAt;
    private String cancellationReason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Long amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Long getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(Long amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(String canceledAt) {
        this.canceledAt = canceledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public static PayOSPaymentLinkResponse from(Map<String, Object> payload) {
        if (payload == null) {
            return null;
        }
        PayOSPaymentLinkResponse response = new PayOSPaymentLinkResponse();
        response.id = getString(payload, "id");
        response.orderCode = getLong(payload, "orderCode");
        response.amount = getLong(payload, "amount");
        response.amountPaid = getLong(payload, "amountPaid");
        response.amountRemaining = getLong(payload, "amountRemaining");
        response.status = getString(payload, "status");
        response.createdAt = getString(payload, "createdAt");
        response.canceledAt = getString(payload, "canceledAt");
        response.cancellationReason = getString(payload, "cancellationReason");
        return response;
    }

    private static String getString(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value != null ? value.toString() : null;
    }

    private static Long getLong(Map<String, Object> source, String key) {
        Object value = source.get(key);
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String str && !str.isBlank()) {
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    public String getUserFriendlyStatus() {
        return switch (status) {
            case "PAID" -> "Paid";
            case "PENDING" -> "Pending";
            case "CANCELED" -> "Canceled";
            default -> "Unknown";
        };
    }
}
