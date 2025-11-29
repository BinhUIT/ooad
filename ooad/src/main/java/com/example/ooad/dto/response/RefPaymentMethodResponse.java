package com.example.ooad.dto.response;

public class RefPaymentMethodResponse {
    private int paymentMethodId;
    private String methodCode;
    private String methodName;
    private String description;
    private boolean isActive;
    private int sortOrder;

    public RefPaymentMethodResponse() {
    }

    public RefPaymentMethodResponse(int paymentMethodId, String methodCode, String methodName, 
                                   String description, boolean isActive, int sortOrder) {
        this.paymentMethodId = paymentMethodId;
        this.methodCode = methodCode;
        this.methodName = methodName;
        this.description = description;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
