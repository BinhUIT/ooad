package com.example.ooad.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysParamGroupResponse {
   private int groupId;
   private String groupCode;
   private String groupName;
   private String description;
   private boolean isActive;
   private int sortOrder;

   public SysParamGroupResponse() {
   }

   public SysParamGroupResponse(int groupId, String groupCode, String groupName,
         String description, boolean isActive, int sortOrder) {
      this.groupId = groupId;
      this.groupCode = groupCode;
      this.groupName = groupName;
      this.description = description;
      this.isActive = isActive;
      this.sortOrder = sortOrder;
   }
}