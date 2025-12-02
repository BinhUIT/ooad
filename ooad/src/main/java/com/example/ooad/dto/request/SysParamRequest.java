package com.example.ooad.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SysParamRequest {

   @NotBlank(message = "Parameter code is required")
   @Size(max = 100, message = "Parameter code must not exceed 100 characters")
   private String paramCode;

   @NotBlank(message = "Parameter name is required")
   @Size(max = 255, message = "Parameter name must not exceed 255 characters")
   private String paramName;

   @Size(max = 1000, message = "Parameter value must not exceed 1000 characters")
   private String paramValue;

   @Size(max = 500, message = "Description must not exceed 500 characters")
   private String description;

   @NotNull(message = "Group ID is required")
   private Integer groupId;

   @NotNull(message = "Active status is required")
   private Boolean isActive;

   public SysParamRequest() {
      this.isActive = true;
   }

   public SysParamRequest(String paramCode, String paramName, String paramValue, String description,
         Integer groupId, Boolean isActive) {
      this.paramCode = paramCode;
      this.paramName = paramName;
      this.paramValue = paramValue;
      this.description = description;
      this.groupId = groupId;
      this.isActive = isActive != null ? isActive : true;
   }

   // Getters and Setters
   public String getParamCode() {
      return paramCode;
   }

   public void setParamCode(String paramCode) {
      this.paramCode = paramCode;
   }

   public String getParamName() {
      return paramName;
   }

   public void setParamName(String paramName) {
      this.paramName = paramName;
   }

   public String getParamValue() {
      return paramValue;
   }

   public void setParamValue(String paramValue) {
      this.paramValue = paramValue;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Integer getGroupId() {
      return groupId;
   }

   public void setGroupId(Integer groupId) {
      this.groupId = groupId;
   }

   public Boolean getIsActive() {
      return isActive;
   }

   public void setIsActive(Boolean isActive) {
      this.isActive = isActive;
   }
}