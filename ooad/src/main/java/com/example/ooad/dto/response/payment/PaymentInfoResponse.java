package com.example.ooad.dto.response.payment;

public class PaymentInfoResponse {
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

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PaymentInfoResponse response = new PaymentInfoResponse();

        public Builder id(String id) {
            response.id = id;
            return this;
        }

        public Builder orderCode(Long orderCode) {
            response.orderCode = orderCode;
            return this;
        }

        public Builder amount(Long amount) {
            response.amount = amount;
            return this;
        }

        public Builder amountPaid(Long amountPaid) {
            response.amountPaid = amountPaid;
            return this;
        }

        public Builder amountRemaining(Long amountRemaining) {
            response.amountRemaining = amountRemaining;
            return this;
        }

        public Builder status(String status) {
            response.status = status;
            return this;
        }

        public Builder createdAt(String createdAt) {
            response.createdAt = createdAt;
            return this;
        }

        public Builder canceledAt(String canceledAt) {
            response.canceledAt = canceledAt;
            return this;
        }

        public Builder cancellationReason(String cancellationReason) {
            response.cancellationReason = cancellationReason;
            return this;
        }

        public PaymentInfoResponse build() {
            return response;
        }
    }
}
