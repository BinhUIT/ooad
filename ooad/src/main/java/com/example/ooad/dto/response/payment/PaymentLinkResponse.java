package com.example.ooad.dto.response.payment;

public class PaymentLinkResponse {
    private String bin;
    private String accountNumber;
    private String accountName;
    private Long amount;
    private String description;
    private Long orderCode;
    private String currency;
    private String paymentLinkId;
    private String status;
    private String checkoutUrl;
    private String qrCode;

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

    public String getPaymentLinkId() {
        return paymentLinkId;
    }

    public void setPaymentLinkId(String paymentLinkId) {
        this.paymentLinkId = paymentLinkId;
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

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PaymentLinkResponse response = new PaymentLinkResponse();

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

        public Builder paymentLinkId(String paymentLinkId) {
            response.paymentLinkId = paymentLinkId;
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

        public PaymentLinkResponse build() {
            return response;
        }
    }
}
