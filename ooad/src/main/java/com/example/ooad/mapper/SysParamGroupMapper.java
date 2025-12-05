package com.example.ooad.mapper;

import com.example.ooad.domain.entity.SysParamGroup;
import com.example.ooad.dto.request.SysParamGroupRequest;
import com.example.ooad.dto.response.SysParamGroupResponse;

public class SysParamGroupMapper {

    public static SysParamGroup fromRequestToEntity(SysParamGroupRequest request) {
        SysParamGroup entity = new SysParamGroup();
        entity.setGroupCode(request.getGroupCode());
        entity.setGroupName(request.getGroupName());
        entity.setDescription(request.getDescription());
        entity.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        return entity;
    }

    public static void updateEntityFromRequest(SysParamGroup entity, SysParamGroupRequest request) {
        if (request.getGroupCode() != null) {
            entity.setGroupCode(request.getGroupCode());
        }
        if (request.getGroupName() != null) {
            entity.setGroupName(request.getGroupName());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getIsActive() != null) {
            entity.setActive(request.getIsActive());
        }
    }

    public static SysParamGroupResponse fromEntityToResponse(SysParamGroup entity) {
        return new SysParamGroupResponse(
                entity.getGroupId(),
                entity.getGroupCode(),
                entity.getGroupName(),
                entity.getDescription(),
                entity.isActive());
    }
}
