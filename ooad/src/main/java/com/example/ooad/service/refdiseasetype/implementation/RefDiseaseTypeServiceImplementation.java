package com.example.ooad.service.refdiseasetype.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.RefDiseaseType;
import com.example.ooad.dto.request.RefDiseaseTypeFilterRequest;
import com.example.ooad.dto.request.RefDiseaseTypeRequest;
import com.example.ooad.dto.response.RefDiseaseTypeResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.mapper.RefDiseaseTypeMapper;
import com.example.ooad.repository.RefDiseaseTypeRepository;
import com.example.ooad.service.refdiseasetype.interfaces.RefDiseaseTypeService;

@Service
public class RefDiseaseTypeServiceImplementation implements RefDiseaseTypeService {

    private final RefDiseaseTypeRepository refDiseaseTypeRepository;

    public RefDiseaseTypeServiceImplementation(RefDiseaseTypeRepository refDiseaseTypeRepository) {
        this.refDiseaseTypeRepository = refDiseaseTypeRepository;
    }

    @Override
    public RefDiseaseTypeResponse createDiseaseType(RefDiseaseTypeRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        if (refDiseaseTypeRepository.existsByDiseaseCode(request.getDiseaseCode())) {
            throw new ConflictException("Disease type with code '" + request.getDiseaseCode() + "' already exists");
        }

        RefDiseaseType entity = RefDiseaseTypeMapper.fromRequestToEntity(request);
        entity = refDiseaseTypeRepository.save(entity);

        return RefDiseaseTypeMapper.fromEntityToResponse(entity);
    }

    @Override
    public RefDiseaseTypeResponse updateDiseaseType(int id, RefDiseaseTypeRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        RefDiseaseType entity = refDiseaseTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Disease type not found with id: " + id));

        if (!entity.getDiseaseCode().equals(request.getDiseaseCode())) {
            if (refDiseaseTypeRepository.existsByDiseaseCode(request.getDiseaseCode())) {
                throw new ConflictException("Disease type with code '" + request.getDiseaseCode() + "' already exists");
            }
        }

        RefDiseaseTypeMapper.updateEntityFromRequest(entity, request);
        entity = refDiseaseTypeRepository.save(entity);

        return RefDiseaseTypeMapper.fromEntityToResponse(entity);
    }

    @Override
    public RefDiseaseTypeResponse getDiseaseTypeById(int id) {
        RefDiseaseType entity = refDiseaseTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Disease type not found with id: " + id));

        return RefDiseaseTypeMapper.fromEntityToResponse(entity);
    }

    @Override
    public Page<RefDiseaseTypeResponse> searchDiseaseTypes(RefDiseaseTypeFilterRequest filter) {
        Sort sort = Sort.by(filter.getSortType() != null && filter.getSortType().equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC,
                filter.getSortBy() != null ? filter.getSortBy() : "diseaseTypeId");

        Pageable pageable = PageRequest.of(filter.getPage() != null ? filter.getPage() : 0,
                filter.getSize() != null ? filter.getSize() : 10,
                sort);

        Specification<RefDiseaseType> spec = (root, query, cb) -> cb.conjunction();

        if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {
            String kw = "%" + filter.getKeyword().trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("diseaseCode")), kw),
                    cb.like(cb.lower(root.get("diseaseName")), kw)));
        }

        if (filter.getIsActive() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"), filter.getIsActive()));
        }

        if (filter.getIsChronic() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isChronic"), filter.getIsChronic()));
        }

        if (filter.getIsContagious() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isContagious"), filter.getIsContagious()));
        }

        Page<RefDiseaseType> page = refDiseaseTypeRepository.findAll(spec, pageable);

        return page.map(RefDiseaseTypeMapper::fromEntityToResponse);
    }

    @Override
    public List<RefDiseaseTypeResponse> getAllDiseaseTypes() {
        return refDiseaseTypeRepository.findAll()
                .stream()
                .map(RefDiseaseTypeMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefDiseaseTypeResponse> getAllActiveDiseaseTypes() {
        return refDiseaseTypeRepository.findAll()
                .stream()
                .filter(RefDiseaseType::isActive)
                .map(RefDiseaseTypeMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDiseaseType(int id) {
        if (!refDiseaseTypeRepository.existsById(id)) {
            throw new NotFoundException("Disease type not found with id: " + id);
        }

        refDiseaseTypeRepository.deleteById(id);
    }
}
