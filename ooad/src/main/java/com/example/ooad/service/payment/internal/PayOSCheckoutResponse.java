package com.example.ooad.service.payment.internal;

import java.util.Map;

public class PayOSCheckoutResponse {
    private String bin;
    private String accountNumber;
    private String accountName;
    private Long amount;
    private String description;
    private Long orderCode;
    private String currency;
    private String status;
    private String checkoutUrl;
    private String qrCode;
    private String paymentLinkId;

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPaymentLinkId() {
        return paymentLinkId;
    }

    public void setPaymentLinkId(String paymentLinkId) {
        this.paymentLinkId = paymentLinkId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PayOSCheckoutResponse from(Map<String, Object> payload) {
        if (payload == null) {
            return null;
        }
        
        // PayOS API v2 returns data in nested "data" object
        Map<String, Object> data = payload;
        if (payload.containsKey("data") && payload.get("data") instanceof Map) {
            data = (Map<String, Object>) payload.get("data");
        }
        
        return builder()
                .bin(getString(data, "bin"))
                .accountNumber(getString(data, "accountNumber"))
                .accountName(getString(data, "accountName"))
                .amount(getLong(data, "amount"))
                .description(getString(data, "description"))
                .orderCode(getLong(data, "orderCode"))
                .currency(getString(data, "currency"))
                .status(getString(data, "status"))
                .checkoutUrl(getString(data, "checkoutUrl"))
                .qrCode(getString(data, "qrCode"))
                .paymentLinkId(getString(data, "paymentLinkId"))
                .build();
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

    public static class Builder {
        private final PayOSCheckoutResponse response = new PayOSCheckoutResponse();

        public Builder bin(String bin) {
            response.bin = bin;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            response.accountNumber = accountNumber;
            return this;
        }

        public Builder accountName(String accountName) {
            response.accountName = accountName;
            return this;
        }

        public Builder amount(Long amount) {
            response.amount = amount;
            return this;
        }

        public Builder description(String description) {
            response.description = description;
            return this;
        }

        public Builder orderCode(Long orderCode) {
            response.orderCode = orderCode;
            return this;
        }

        public Builder currency(String currency) {
            response.currency = currency;
            return this;
        }

        public Builder status(String status) {
            response.status = status;
            return this;
        }

        public Builder checkoutUrl(String checkoutUrl) {
            response.checkoutUrl = checkoutUrl;
            return this;
        }

        public Builder qrCode(String qrCode) {
            response.qrCode = qrCode;
            return this;
        }

        public Builder paymentLinkId(String paymentLinkId) {
            response.paymentLinkId = paymentLinkId;
            return this;
        }

        public PayOSCheckoutResponse build() {
            return response;
        }
    }
}
