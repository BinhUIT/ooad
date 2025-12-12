package com.example.ooad.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {

    @JsonProperty("serviceName")
    private String serviceName;

    @JsonProperty("unitPrice")
    private float unitPrice;

}
