package com.example.ooad.service.sysparam.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.example.ooad.dto.request.SysParamRequest;
import com.example.ooad.dto.response.SysParamResponse;

public interface SysParamService {
   SysParamResponse createSysParam(SysParamRequest request, BindingResult bindingResult);

   SysParamResponse updateSysParam(int id, SysParamRequest request, BindingResult bindingResult);

   SysParamResponse getSysParamById(int id);

   SysParamResponse getSysParamByCode(String paramCode);

   List<SysParamResponse> getAllSysParams();

   List<SysParamResponse> getAllActiveSysParams();

   List<SysParamResponse> getSysParamsByGroupId(int groupId);

   List<SysParamResponse> getSysParamsByGroupCode(String groupCode);

   Page<SysParamResponse> searchSysParams(int page, int size, String keyword,
         Integer groupId);

   void deleteSysParam(int id);
}