package com.example.ooad.service.sysparam.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.example.ooad.dto.request.SysParamGroupRequest;
import com.example.ooad.dto.response.SysParamGroupResponse;

public interface SysParamGroupService {
   SysParamGroupResponse createSysParamGroup(SysParamGroupRequest request, BindingResult bindingResult);

   SysParamGroupResponse updateSysParamGroup(int id, SysParamGroupRequest request, BindingResult bindingResult);

   SysParamGroupResponse getSysParamGroupById(int id);

   SysParamGroupResponse getSysParamGroupByCode(String groupCode);

   List<SysParamGroupResponse> getAllSysParamGroups();

   List<SysParamGroupResponse> getAllActiveSysParamGroups();

   Page<SysParamGroupResponse> searchSysParamGroups(int page, int size, String keyword);

   void deleteSysParamGroup(int id);
}