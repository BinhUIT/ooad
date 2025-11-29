package com.example.ooad.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RefPaymentMethodRequest {
    
    @NotBlank(message = "Method code is required")
    @Size(max = 50, message = "Method code must not exceed 50 characters")
    private String methodCode;
    
    @NotBlank(message = "Method name is required")
    @Size(max = 100, message = "Method name must not exceed 100 characters")
    private String methodName;
    
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
    
    @NotNull(message = "Active status is required")
    private Boolean isActive;
    
    private Integer sortOrder;

    public RefPaymentMethodRequest() {
        this.isActive = false;
        this.sortOrder = 0;
    }

    public RefPaymentMethodRequest(String methodCode, String methodName, String description, Boolean isActive, Integer sortOrder) {
        this.methodCode = methodCode;
        this.methodName = methodName;
        this.description = description;
        this.isActive = isActive != null ? isActive : false;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
