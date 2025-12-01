package com.example.ooad.service.sysparam.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.ooad.domain.entity.SysParamGroup;
import com.example.ooad.dto.request.SysParamGroupRequest;
import com.example.ooad.dto.response.SysParamGroupResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.mapper.SysParamGroupMapper;
import com.example.ooad.repository.SysParamGroupRepository;
import com.example.ooad.service.sysparam.interfaces.SysParamGroupService;

@Service
public class SysParamGroupServiceImplementation implements SysParamGroupService {

   private final SysParamGroupRepository sysParamGroupRepository;

   public SysParamGroupServiceImplementation(SysParamGroupRepository sysParamGroupRepository) {
      this.sysParamGroupRepository = sysParamGroupRepository;
   }

   @Override
   public SysParamGroupResponse createSysParamGroup(SysParamGroupRequest request, BindingResult bindingResult) {
      // Validate request
      if (bindingResult.hasErrors()) {
         throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
      }

      // Check if group code already exists
      if (sysParamGroupRepository.existsByGroupCode(request.getGroupCode())) {
         throw new ConflictException(
               "System parameter group with code '" + request.getGroupCode() + "' already exists");
      }

      // Create and save entity
      SysParamGroup entity = SysParamGroupMapper.fromRequestToEntity(request);
      entity = sysParamGroupRepository.save(entity);

      // Return response
      return SysParamGroupMapper.fromEntityToResponse(entity);
   }

   @Override
   public SysParamGroupResponse updateSysParamGroup(int id, SysParamGroupRequest request, BindingResult bindingResult) {
      // Validate request
      if (bindingResult.hasErrors()) {
         throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
      }

      // Find existing entity
      SysParamGroup entity = sysParamGroupRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("System parameter group not found with id: " + id));

      // Check if group code is being changed and if new code already exists
      if (!entity.getGroupCode().equals(request.getGroupCode())) {
         if (sysParamGroupRepository.existsByGroupCode(request.getGroupCode())) {
            throw new ConflictException(
                  "System parameter group with code '" + request.getGroupCode() + "' already exists");
         }
      }

      // Update entity
      SysParamGroupMapper.updateEntityFromRequest(entity, request);
      entity = sysParamGroupRepository.save(entity);

      // Return response
      return SysParamGroupMapper.fromEntityToResponse(entity);
   }

   @Override
   public SysParamGroupResponse getSysParamGroupById(int id) {
      SysParamGroup entity = sysParamGroupRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("System parameter group not found with id: " + id));
      return SysParamGroupMapper.fromEntityToResponse(entity);
   }

   @Override
   public SysParamGroupResponse getSysParamGroupByCode(String groupCode) {
      SysParamGroup entity = sysParamGroupRepository.findByGroupCode(groupCode)
            .orElseThrow(() -> new NotFoundException("System parameter group not found with code: " + groupCode));
      return SysParamGroupMapper.fromEntityToResponse(entity);
   }

   @Override
   public List<SysParamGroupResponse> getAllSysParamGroups() {
      return sysParamGroupRepository.findAll()
            .stream()
            .map(SysParamGroupMapper::fromEntityToResponse)
            .collect(Collectors.toList());
   }

   @Override
   public List<SysParamGroupResponse> getAllActiveSysParamGroups() {
      return sysParamGroupRepository.findAllActive()
            .stream()
            .map(SysParamGroupMapper::fromEntityToResponse)
            .collect(Collectors.toList());
   }

   @Override
   public Page<SysParamGroupResponse> searchSysParamGroups(int page, int size,
         String keyword) {
      Pageable pageable = PageRequest.of(page, size);
      org.springframework.data.domain.Page<SysParamGroup> entities;

      if (keyword != null && !keyword.trim().isEmpty()) {
         entities = sysParamGroupRepository.findByGroupCodeContainingOrGroupNameContaining(keyword, keyword, pageable);
      } else {
         entities = sysParamGroupRepository.findAll(pageable);
      }

      return entities.map(SysParamGroupMapper::fromEntityToResponse);
   }

   @Override
   public void deleteSysParamGroup(int id) {
      if (!sysParamGroupRepository.existsById(id)) {
         throw new NotFoundException("System parameter group not found with id: " + id);
      }
      sysParamGroupRepository.deleteById(id);
   }
}