package com.example.ooad.dto.response;

public class OrderedServiceDto {
    private Integer serviceId;
    private String serviceName;
    private Integer quantity;

    public OrderedServiceDto() {
    }

    public OrderedServiceDto(Integer serviceId, String serviceName, Integer quantity) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.quantity = quantity;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
