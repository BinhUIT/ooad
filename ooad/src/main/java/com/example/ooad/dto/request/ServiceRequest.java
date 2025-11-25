package com.example.ooad.dto.request;

import java.math.BigDecimal;

public class ServiceRequest {
    private String serviceName;
    private BigDecimal unitPrice;
    public ServiceRequest(String serviceName, BigDecimal unitPrice) {
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
    }
    public ServiceRequest() {

    } 
    public String getServiceName() {
        return this.serviceName;
    } 
    public BigDecimal getUnitPrice() {
        return this.unitPrice; 
    } 
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName; 
    } 
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice= unitPrice;
    }
}
