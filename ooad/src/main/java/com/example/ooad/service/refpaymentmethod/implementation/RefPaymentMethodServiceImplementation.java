package com.example.ooad.service.refpaymentmethod.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.RefPaymentMethod;
import com.example.ooad.dto.request.RefPaymentMethodFilterRequest;
import com.example.ooad.dto.request.RefPaymentMethodRequest;
import com.example.ooad.dto.response.RefPaymentMethodResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.mapper.RefPaymentMethodMapper;
import com.example.ooad.repository.RefPaymentMethodRepository;
import com.example.ooad.service.refpaymentmethod.interfaces.RefPaymentMethodService;

@Service
public class RefPaymentMethodServiceImplementation implements RefPaymentMethodService {
    
    private final RefPaymentMethodRepository refPaymentMethodRepository;

    public RefPaymentMethodServiceImplementation(RefPaymentMethodRepository refPaymentMethodRepository) {
        this.refPaymentMethodRepository = refPaymentMethodRepository;
    }

    @Override
    public RefPaymentMethodResponse createPaymentMethod(RefPaymentMethodRequest request, BindingResult bindingResult) {
        // Validate request
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Check if method code already exists
        if (refPaymentMethodRepository.existsByMethodCode(request.getMethodCode())) {
            throw new ConflictException("Payment method with code '" + request.getMethodCode() + "' already exists");
        }

        // Create and save entity
        RefPaymentMethod entity = RefPaymentMethodMapper.fromRequestToEntity(request);
        entity = refPaymentMethodRepository.save(entity);

        // Return response
        return RefPaymentMethodMapper.fromEntityToResponse(entity);
    }

    @Override
    public RefPaymentMethodResponse updatePaymentMethod(int id, RefPaymentMethodRequest request, BindingResult bindingResult) {
        // Validate request
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Find existing entity
        RefPaymentMethod entity = refPaymentMethodRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Payment method not found with id: " + id));

        // Check if method code is being changed and if new code already exists
        if (!entity.getMethodCode().equals(request.getMethodCode())) {
            if (refPaymentMethodRepository.existsByMethodCode(request.getMethodCode())) {
                throw new ConflictException("Payment method with code '" + request.getMethodCode() + "' already exists");
            }
        }

        // Update entity
        RefPaymentMethodMapper.updateEntityFromRequest(entity, request);
        entity = refPaymentMethodRepository.save(entity);

        // Return response
        return RefPaymentMethodMapper.fromEntityToResponse(entity);
    }

    @Override
    public RefPaymentMethodResponse getPaymentMethodById(int id) {
        RefPaymentMethod entity = refPaymentMethodRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Payment method not found with id: " + id));
        
        return RefPaymentMethodMapper.fromEntityToResponse(entity);
    }

    @Override
    public Page<RefPaymentMethodResponse> searchPaymentMethods(RefPaymentMethodFilterRequest filter) {
        // Build sort
        Sort sort = Sort.by(filter.getSortType() != null && filter.getSortType().equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC,
            filter.getSortBy() != null ? filter.getSortBy() : "paymentMethodId");

        Pageable pageable = PageRequest.of(filter.getPage() != null ? filter.getPage() : 0,
                filter.getSize() != null ? filter.getSize() : 10,
                sort);

        // Build specification
        Specification<RefPaymentMethod> spec = (root, query, cb) -> cb.conjunction();

        if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {
            String kw = "%" + filter.getKeyword().trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("methodCode")), kw),
                cb.like(cb.lower(root.get("methodName")), kw)
            ));
        }

        if (filter.getIsActive() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"), filter.getIsActive()));
        }

        Page<RefPaymentMethod> page = refPaymentMethodRepository.findAll(spec, pageable);

        return page.map(RefPaymentMethodMapper::fromEntityToResponse);
    }

    @Override
    public List<RefPaymentMethodResponse> getAllPaymentMethods() {
        return refPaymentMethodRepository.findAll()
            .stream()
            .map(RefPaymentMethodMapper::fromEntityToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<RefPaymentMethodResponse> getAllActivePaymentMethods() {
        return refPaymentMethodRepository.findAll()
            .stream()
            .filter(RefPaymentMethod::isActive)
            .map(RefPaymentMethodMapper::fromEntityToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public void deletePaymentMethod(int id) {
        if (!refPaymentMethodRepository.existsById(id)) {
            throw new NotFoundException("Payment method not found with id: " + id);
        }
        
        refPaymentMethodRepository.deleteById(id);
    }
}
