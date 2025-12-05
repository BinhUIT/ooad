package com.example.ooad.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysParamRequest {

    @NotBlank(message = "Parameter code is required")
    @Size(max = 100, message = "Parameter code must not exceed 100 characters")
    private String paramCode;

    @NotBlank(message = "Parameter name is required")
    @Size(max = 255, message = "Parameter name must not exceed 255 characters")
    private String paramName;

    @Size(max = 1000, message = "Parameter value must not exceed 1000 characters")
    private String paramValue;

    @Size(max = 50, message = "Data type must not exceed 50 characters")
    private String dataType;

    @Size(max = 50, message = "Unit must not exceed 50 characters")
    private String unit;

    private LocalDate effectiveFrom;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Group ID is required")
    private Integer groupId;

    @NotNull(message = "Active status is required")
    @JsonProperty("active")
    private Boolean isActive;

    public SysParamRequest() {
        this.isActive = true;
        this.dataType = "STRING";
        this.effectiveFrom = LocalDate.now();
    }

    public SysParamRequest(String paramCode, String paramName, String paramValue, String dataType,
            String unit, LocalDate effectiveFrom, String description, Integer groupId, Boolean isActive) {
        this.paramCode = paramCode;
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.dataType = dataType != null ? dataType : "STRING";
        this.unit = unit;
        this.effectiveFrom = effectiveFrom != null ? effectiveFrom : LocalDate.now();
        this.description = description;
        this.groupId = groupId;
        this.isActive = isActive != null ? isActive : true;
    }
}
