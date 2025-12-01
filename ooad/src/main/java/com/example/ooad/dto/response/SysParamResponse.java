package com.example.ooad.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysParamResponse {
   private int paramId;
   private String paramCode;
   private String paramName;
   private String paramValue;
   private String description;
   private int groupId;
   private String groupCode;
   private String groupName;
   private boolean isActive;
   private int sortOrder;

   public SysParamResponse() {
   }

   public SysParamResponse(int paramId, String paramCode, String paramName, String paramValue,
         String description, int groupId, String groupCode, String groupName,
         boolean isActive, int sortOrder) {
      this.paramId = paramId;
      this.paramCode = paramCode;
      this.paramName = paramName;
      this.paramValue = paramValue;
      this.description = description;
      this.groupId = groupId;
      this.groupCode = groupCode;
      this.groupName = groupName;
      this.isActive = isActive;
      this.sortOrder = sortOrder;
   }
}