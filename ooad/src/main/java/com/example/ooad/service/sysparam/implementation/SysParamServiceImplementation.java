package com.example.ooad.service.sysparam.implementation;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.SysParam;
import com.example.ooad.domain.entity.SysParamGroup;
import com.example.ooad.dto.request.SysParamRequest;
import com.example.ooad.dto.response.SysParamResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.mapper.SysParamMapper;
import com.example.ooad.repository.SysParamRepository;
import com.example.ooad.repository.SysParamGroupRepository;
import com.example.ooad.service.sysparam.TypedParamValue;
import com.example.ooad.service.sysparam.interfaces.SysParamService;

@Service
public class SysParamServiceImplementation implements SysParamService {

    private final SysParamRepository sysParamRepository;
    private final SysParamGroupRepository sysParamGroupRepository;

    public SysParamServiceImplementation(SysParamRepository sysParamRepository,
            SysParamGroupRepository sysParamGroupRepository) {
        this.sysParamRepository = sysParamRepository;
        this.sysParamGroupRepository = sysParamGroupRepository;
    }

    @Override
    public SysParamResponse createSysParam(SysParamRequest request, BindingResult bindingResult) {
        // Validate request
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Check if param code already exists
        if (sysParamRepository.existsByParamCode(request.getParamCode())) {
            throw new ConflictException("System parameter with code '" + request.getParamCode() + "' already exists");
        }

        // Get group
        SysParamGroup group = sysParamGroupRepository.findById(request.getGroupId())
                .orElseThrow(
                        () -> new NotFoundException(
                                "System parameter group not found with id: " + request.getGroupId()));

        // Create and save entity
        SysParam entity = SysParamMapper.fromRequestToEntity(request, group);
        entity = sysParamRepository.save(entity);

        // Return response
        return SysParamMapper.fromEntityToResponse(entity);
    }

    @Override
    public SysParamResponse updateSysParam(int id, SysParamRequest request, BindingResult bindingResult) {
        // Validate request
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Find existing entity
        SysParam entity = sysParamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("System parameter not found with id: " + id));

        // Check if param code is being changed and if new code already exists
        if (!entity.getParamCode().equals(request.getParamCode())) {
            if (sysParamRepository.existsByParamCode(request.getParamCode())) {
                throw new ConflictException(
                        "System parameter with code '" + request.getParamCode() + "' already exists");
            }
        }

        // Get group if provided
        SysParamGroup group = null;
        if (request.getGroupId() != null) {
            group = sysParamGroupRepository.findById(request.getGroupId())
                    .orElseThrow(
                            () -> new NotFoundException(
                                    "System parameter group not found with id: " + request.getGroupId()));
        }

        // Update entity
        SysParamMapper.updateEntityFromRequest(entity, request, group);
        entity = sysParamRepository.save(entity);

        // Return response
        return SysParamMapper.fromEntityToResponse(entity);
    }

    @Override
    public SysParamResponse getSysParamById(int id) {
        SysParam entity = sysParamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("System parameter not found with id: " + id));
        return SysParamMapper.fromEntityToResponse(entity);
    }

    @Override
    public SysParamResponse getSysParamByCode(String paramCode) {
        SysParam entity = sysParamRepository.findByParamCode(paramCode)
                .orElseThrow(() -> new NotFoundException("System parameter not found with code: " + paramCode));
        return SysParamMapper.fromEntityToResponse(entity);
    }

    @Override
    public List<SysParamResponse> getAllSysParams() {
        return sysParamRepository.findAll()
                .stream()
                .map(SysParamMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysParamResponse> getAllActiveSysParams() {
        return sysParamRepository.findAllActive()
                .stream()
                .map(SysParamMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysParamResponse> getSysParamsByGroupId(int groupId) {
        return sysParamRepository.findByGroupId(groupId)
                .stream()
                .map(SysParamMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysParamResponse> getSysParamsByGroupCode(String groupCode) {
        return sysParamRepository.findByGroupCode(groupCode)
                .stream()
                .map(SysParamMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<SysParamResponse> searchSysParams(int page, int size, String keyword,
            Integer groupId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SysParam> entities;

        if (keyword != null && !keyword.trim().isEmpty() && groupId != null) {
            entities = sysParamRepository.findByParamCodeContainingOrParamNameContainingAndGroupId(keyword, keyword,
                    groupId, pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            entities = sysParamRepository.findByParamCodeContainingOrParamNameContaining(keyword, keyword, pageable);
        } else if (groupId != null) {
            entities = sysParamRepository.findByGroupId(groupId, pageable);
        } else {
            entities = sysParamRepository.findAll(pageable);
        }

        return entities.map(SysParamMapper::fromEntityToResponse);
    }

    @Override
    public void deleteSysParam(int id) {
        if (!sysParamRepository.existsById(id)) {
            throw new NotFoundException("System parameter not found with id: " + id);
        }
        sysParamRepository.deleteById(id);
    }

    @Override
    public TypedParamValue getTypedParamValueByCode(String paramCode) {
        SysParam entity = sysParamRepository.findByParamCode(paramCode)
                .orElseThrow(() -> new NotFoundException("System parameter not found with code: " + paramCode));
        String type = entity.getDataType();
        String valueStr = entity.getParamValue();
        Object value;
        switch (type.toUpperCase()) {
            case "NUMBER":
                try {
                    value = Integer.parseInt(valueStr);
                } catch (NumberFormatException e) {
                    value = Double.parseDouble(valueStr);
                }
                break;
            case "BOOLEAN":
                value = Boolean.parseBoolean(valueStr);
                break;
            case "TIME":
                value = LocalTime.parse(valueStr); // valueStr phải đúng định dạng HH:mm
                break;
            default:
                value = valueStr;
        }
        return new TypedParamValue(value, type);
    }
}
