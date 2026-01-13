package com.example.ooad.service.inventory.implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.ImportDetail;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.MedicineImport;
import com.example.ooad.domain.entity.MedicineInventory;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.request.inventory.ImportDetailRequest;
import com.example.ooad.dto.request.inventory.MedicineImportFilterRequest;
import com.example.ooad.dto.request.inventory.MedicineImportRequest;
import com.example.ooad.dto.response.inventory.MedicineImportDetailResponse;
import com.example.ooad.dto.response.inventory.MedicineImportListResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.mapper.InventoryMapper;
import com.example.ooad.repository.ImportDetailRepository;
import com.example.ooad.repository.MedicineImportRepository;
import com.example.ooad.repository.MedicineInventoryRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.service.inventory.interfaces.MedicineImportService;

@Service
public class MedicineImportServiceImplementation implements MedicineImportService {
    
    private final MedicineImportRepository medicineImportRepository;
    private final ImportDetailRepository importDetailRepository;
    private final MedicineInventoryRepository medicineInventoryRepository;
    private final MedicineRepository medicineRepository;
    private final StaffRepository staffRepository;
    
    public MedicineImportServiceImplementation(
            MedicineImportRepository medicineImportRepository,
            ImportDetailRepository importDetailRepository,
            MedicineInventoryRepository medicineInventoryRepository,
            MedicineRepository medicineRepository,
            StaffRepository staffRepository) {
        this.medicineImportRepository = medicineImportRepository;
        this.importDetailRepository = importDetailRepository;
        this.medicineInventoryRepository = medicineInventoryRepository;
        this.medicineRepository = medicineRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public Page<MedicineImportListResponse> getImportList(MedicineImportFilterRequest filter) {
        // Build sort
        Sort sort = Sort.by(
            filter.getSortType() != null && filter.getSortType().equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC,
            filter.getSortBy() != null ? filter.getSortBy() : "importId"
        );
        
        Pageable pageable = PageRequest.of(
            filter.getPage() != null ? filter.getPage() : 0,
            filter.getSize() != null ? filter.getSize() : 10,
            sort
        );
        
        // Build specification
        Specification<MedicineImport> spec = Specification.where(null);
        
        // Filter by date range
        if (filter.getFromDate() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("importDate"), filter.getFromDate()));
        }
        
        if (filter.getToDate() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.lessThanOrEqualTo(root.get("importDate"), filter.getToDate()));
        }
        
        // Filter by supplier name
        if (filter.getSupplierName() != null && !filter.getSupplierName().trim().isEmpty()) {
            String supplierName = "%" + filter.getSupplierName().trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("supplier")), supplierName));
        }
        
        // Search by keyword (supplier name)
        if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {
            String kw = "%" + filter.getKeyword().trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("supplier")), kw));
        }
        
        Page<MedicineImport> page = medicineImportRepository.findAll(spec, pageable);
        
        return page.map(InventoryMapper::toMedicineImportListResponse);
    }

    @Override
    public List<MedicineImportListResponse> getAllImports() {
        return medicineImportRepository.findAll().stream()
            .map(InventoryMapper::toMedicineImportListResponse)
            .collect(Collectors.toList());
    }

    @Override
    public MedicineImportDetailResponse getImportDetail(int importId) {
        MedicineImport medicineImport = medicineImportRepository.findById(importId)
            .orElseThrow(() -> new NotFoundException("Medicine import not found with id: " + importId));
        
        List<ImportDetail> details = importDetailRepository.findByImportId(importId);
        
        return InventoryMapper.toMedicineImportDetailResponse(medicineImport, details);
    }

    @Override
    @Transactional
    public MedicineImportDetailResponse createImport(MedicineImportRequest request, BindingResult bindingResult, int importerId) {
        // Validate request
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        
        // Validate details
        if (request.getDetails() == null || request.getDetails().isEmpty()) {
            throw new BadRequestException("Import must have at least one detail");
        }
        
        // Get importer staff
        Staff importer = staffRepository.findById(importerId)
            .orElseThrow(() -> new NotFoundException("Staff not found with id: " + importerId));
        
        // Create medicine import entity
        MedicineImport medicineImport = InventoryMapper.toMedicineImportEntity(request);
        medicineImport.setImporter(importer);
        
        // Calculate totals
        int totalQuantity = 0;
        BigDecimal totalValue = BigDecimal.ZERO;
        
        for (ImportDetailRequest detail : request.getDetails()) {
            totalQuantity += detail.getQuantity();
            BigDecimal detailTotal = detail.getImportPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
            totalValue = totalValue.add(detailTotal);
        }
        
        medicineImport.setTotalQuantity(totalQuantity);
        medicineImport.setTotalValue(totalValue);
        
        // Save medicine import first to get ID
        medicineImport = medicineImportRepository.save(medicineImport);
        
        // Create import details and inventory entries
        for (ImportDetailRequest detailRequest : request.getDetails()) {
            // Get medicine
            Medicine medicine = medicineRepository.findById(detailRequest.getMedicineId())
                .orElseThrow(() -> new NotFoundException("Medicine not found with id: " + detailRequest.getMedicineId()));
            
            // Create import detail
            ImportDetail detail = InventoryMapper.toImportDetailEntity(detailRequest, medicineImport, medicine);
            importDetailRepository.save(detail);
            
            // Create inventory entry
            MedicineInventory inventory = InventoryMapper.toMedicineInventoryEntity(detailRequest, medicineImport, medicine);
            medicineInventoryRepository.save(inventory);
        }
        
        // Return response
        List<ImportDetail> savedDetails = importDetailRepository.findByImportId(medicineImport.getImportId());
        return InventoryMapper.toMedicineImportDetailResponse(medicineImport, savedDetails);
    }

    @Override
    @Transactional
    public MedicineImportDetailResponse updateImport(int importId, MedicineImportRequest request, BindingResult bindingResult) {
        // Validate request
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        
        // Find existing import
        MedicineImport medicineImport = medicineImportRepository.findById(importId)
            .orElseThrow(() -> new NotFoundException("Medicine import not found with id: " + importId));
        
        // Delete old details and inventory entries
        importDetailRepository.deleteByImportId(importId);
        medicineInventoryRepository.deleteByImportId(importId);
        
        // Update import info
        InventoryMapper.updateMedicineImportEntity(medicineImport, request);
        
        // Calculate new totals
        int totalQuantity = 0;
        BigDecimal totalValue = BigDecimal.ZERO;
        
        for (ImportDetailRequest detail : request.getDetails()) {
            totalQuantity += detail.getQuantity();
            BigDecimal detailTotal = detail.getImportPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
            totalValue = totalValue.add(detailTotal);
        }
        
        medicineImport.setTotalQuantity(totalQuantity);
        medicineImport.setTotalValue(totalValue);
        
        // Save updated import
        medicineImport = medicineImportRepository.save(medicineImport);
        
        // Create new import details and inventory entries
        for (ImportDetailRequest detailRequest : request.getDetails()) {
            // Get medicine
            Medicine medicine = medicineRepository.findById(detailRequest.getMedicineId())
                .orElseThrow(() -> new NotFoundException("Medicine not found with id: " + detailRequest.getMedicineId()));
            
            // Create import detail
            ImportDetail detail = InventoryMapper.toImportDetailEntity(detailRequest, medicineImport, medicine);
            importDetailRepository.save(detail);
            
            // Create inventory entry
            MedicineInventory inventory = InventoryMapper.toMedicineInventoryEntity(detailRequest, medicineImport, medicine);
            medicineInventoryRepository.save(inventory);
        }
        
        // Return response
        List<ImportDetail> savedDetails = importDetailRepository.findByImportId(medicineImport.getImportId());
        return InventoryMapper.toMedicineImportDetailResponse(medicineImport, savedDetails);
    }

    @Override
    @Transactional
    public void deleteImport(int importId) {
        // Check if import exists
        if (!medicineImportRepository.existsById(importId)) {
            throw new NotFoundException("Medicine import not found with id: " + importId);
        }
        
        // Delete inventory entries first (foreign key constraint)
        medicineInventoryRepository.deleteByImportId(importId);
        
        // Delete import details
        importDetailRepository.deleteByImportId(importId);
        
        // Delete import
        medicineImportRepository.deleteById(importId);
    }
}
