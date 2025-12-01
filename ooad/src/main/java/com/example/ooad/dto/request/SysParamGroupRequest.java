package com.example.ooad.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysParamGroupRequest {

   @NotBlank(message = "Group code is required")
   @Size(max = 100, message = "Group code must not exceed 100 characters")
   private String groupCode;

   @NotBlank(message = "Group name is required")
   @Size(max = 255, message = "Group name must not exceed 255 characters")
   private String groupName;

   @Size(max = 500, message = "Description must not exceed 500 characters")
   private String description;

   @NotNull(message = "Active status is required")
   private Boolean isActive;

   private Integer sortOrder;

   public SysParamGroupRequest() {
      this.isActive = true;
      this.sortOrder = 0;
   }

   public SysParamGroupRequest(String groupCode, String groupName, String description,
         Boolean isActive, Integer sortOrder) {
      this.groupCode = groupCode;
      this.groupName = groupName;
      this.description = description;
      this.isActive = isActive != null ? isActive : true;
      this.sortOrder = sortOrder != null ? sortOrder : 0;
   }
}