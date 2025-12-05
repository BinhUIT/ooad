package com.example.ooad.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysParamGroupResponse {
    private int groupId;
    private String groupCode;
    private String groupName;
    private String description;
    @JsonProperty("active")
    private boolean isActive;

    public SysParamGroupResponse() {
    }

    public SysParamGroupResponse(int groupId, String groupCode, String groupName,
            String description, boolean isActive) {
        this.groupId = groupId;
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.description = description;
        this.isActive = isActive;
    }
}
