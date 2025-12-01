package com.example.ooad.mapper;

import com.example.ooad.domain.entity.SysParam;
import com.example.ooad.domain.entity.SysParamGroup;
import com.example.ooad.dto.request.SysParamRequest;
import com.example.ooad.dto.response.SysParamResponse;

public class SysParamMapper {

   public static SysParam fromRequestToEntity(SysParamRequest request, SysParamGroup group) {
      SysParam entity = new SysParam();
      entity.setParamCode(request.getParamCode());
      entity.setParamName(request.getParamName());
      entity.setParamValue(request.getParamValue());
      entity.setDescription(request.getDescription());
      entity.setGroup(group);
      entity.setActive(request.getIsActive() != null ? request.getIsActive() : true);
      entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
      return entity;
   }

   public static void updateEntityFromRequest(SysParam entity, SysParamRequest request, SysParamGroup group) {
      if (request.getParamCode() != null) {
         entity.setParamCode(request.getParamCode());
      }
      if (request.getParamName() != null) {
         entity.setParamName(request.getParamName());
      }
      if (request.getParamValue() != null) {
         entity.setParamValue(request.getParamValue());
      }
      if (request.getDescription() != null) {
         entity.setDescription(request.getDescription());
      }
      if (group != null) {
         entity.setGroup(group);
      }
      if (request.getIsActive() != null) {
         entity.setActive(request.getIsActive());
      }
      if (request.getSortOrder() != null) {
         entity.setSortOrder(request.getSortOrder());
      }
   }

   public static SysParamResponse fromEntityToResponse(SysParam entity) {
      return new SysParamResponse(
            entity.getParamId(),
            entity.getParamCode(),
            entity.getParamName(),
            entity.getParamValue(),
            entity.getDescription(),
            entity.getGroup().getGroupId(),
            entity.getGroup().getGroupCode(),
            entity.getGroup().getGroupName(),
            entity.isActive(),
            entity.getSortOrder());
   }
}