package com.example.ooad.service.inventory.implementation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        List<MedicineInventory> inventories = medicineInventoryRepository.findByImportId(importId);
        
        // Build a map of medicineId -> MedicineInventory for quick lookup
        Map<Integer, MedicineInventory> inventoryMap = new HashMap<>();
        for (MedicineInventory inv : inventories) {
            inventoryMap.put(inv.getMedicine().getMedicineId(), inv);
        }
        
        return InventoryMapper.toMedicineImportDetailResponse(medicineImport, details, inventoryMap);
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
            
            // Create inventory entry (with same quantity as import detail)
            MedicineInventory inventory = InventoryMapper.toMedicineInventoryEntity(detailRequest, medicineImport, medicine);
            medicineInventoryRepository.save(inventory);
        }
        
        // Return response with proper inventory map
        List<ImportDetail> savedDetails = importDetailRepository.findByImportId(medicineImport.getImportId());
        List<MedicineInventory> savedInventories = medicineInventoryRepository.findByImportId(medicineImport.getImportId());
        
        Map<Integer, MedicineInventory> inventoryMap = new HashMap<>();
        for (MedicineInventory inv : savedInventories) {
            inventoryMap.put(inv.getMedicine().getMedicineId(), inv);
        }
        
        return InventoryMapper.toMedicineImportDetailResponse(medicineImport, savedDetails, inventoryMap);
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
        
        // Get existing details and inventories
        List<ImportDetail> existingDetails = importDetailRepository.findByImportId(importId);
        List<MedicineInventory> existingInventories = medicineInventoryRepository.findByImportId(importId);
        
        // Build maps for quick lookup
        Map<Integer, ImportDetail> existingDetailMap = new HashMap<>();
        for (ImportDetail d : existingDetails) {
            existingDetailMap.put(d.getMedicine().getMedicineId(), d);
        }
        
        Map<Integer, MedicineInventory> existingInventoryMap = new HashMap<>();
        for (MedicineInventory inv : existingInventories) {
            existingInventoryMap.put(inv.getMedicine().getMedicineId(), inv);
        }
        
        // Collect medicine IDs from request
        Set<Integer> requestMedicineIds = new HashSet<>();
        for (ImportDetailRequest detailReq : request.getDetails()) {
            requestMedicineIds.add(detailReq.getMedicineId());
        }
        
        // Check which existing items are being removed and validate they're editable
        for (ImportDetail existingDetail : existingDetails) {
            int medicineId = existingDetail.getMedicine().getMedicineId();
            if (!requestMedicineIds.contains(medicineId)) {
                // This item is being removed - check if it's editable
                MedicineInventory inv = existingInventoryMap.get(medicineId);
                if (inv != null && inv.getQuantityInStock() != existingDetail.getQuantity()) {
                    throw new BadRequestException("Không thể xóa thuốc '" + existingDetail.getMedicine().getMedicineName() + 
                        "' vì đã bán " + (existingDetail.getQuantity() - inv.getQuantityInStock()) + " đơn vị.");
                }
            }
        }
        
        // Delete only editable items (items that haven't been sold)
        for (ImportDetail existingDetail : existingDetails) {
            int medicineId = existingDetail.getMedicine().getMedicineId();
            MedicineInventory inv = existingInventoryMap.get(medicineId);
            
            // Only delete if editable (no items sold) OR if it's being replaced with new data
            if (inv != null && inv.getQuantityInStock() == existingDetail.getQuantity()) {
                importDetailRepository.deleteByImportIdAndMedicineId(importId, medicineId);
                medicineInventoryRepository.deleteByImportIdAndMedicineId(importId, medicineId);
            } else if (!requestMedicineIds.contains(medicineId)) {
                // If item was sold and is being removed from request, we already threw error above
            }
            // If item was sold but is in request, we'll update it below (but only header info, not quantity)
        }
        
        // Update import info
        InventoryMapper.updateMedicineImportEntity(medicineImport, request);
        
        // Calculate new totals
        int totalQuantity = 0;
        BigDecimal totalValue = BigDecimal.ZERO;
        
        // Process each detail in request
        for (ImportDetailRequest detailRequest : request.getDetails()) {
            ImportDetail existingDetail = existingDetailMap.get(detailRequest.getMedicineId());
            MedicineInventory existingInv = existingInventoryMap.get(detailRequest.getMedicineId());
            
            // Get medicine
            Medicine medicine = medicineRepository.findById(detailRequest.getMedicineId())
                .orElseThrow(() -> new NotFoundException("Medicine not found with id: " + detailRequest.getMedicineId()));
            
            if (existingDetail != null && existingInv != null) {
                // Existing item - check if editable
                boolean isEditable = existingInv.getQuantityInStock() == existingDetail.getQuantity();
                
                if (isEditable) {
                    // Editable - we already deleted, now create new
                    ImportDetail newDetail = InventoryMapper.toImportDetailEntity(detailRequest, medicineImport, medicine);
                    importDetailRepository.save(newDetail);
                    
                    MedicineInventory newInventory = InventoryMapper.toMedicineInventoryEntity(detailRequest, medicineImport, medicine);
                    medicineInventoryRepository.save(newInventory);
                    
                    totalQuantity += detailRequest.getQuantity();
                    totalValue = totalValue.add(detailRequest.getImportPrice().multiply(BigDecimal.valueOf(detailRequest.getQuantity())));
                } else {
                    // Not editable - keep original values, only update non-quantity fields if needed
                    // For now, we throw an error if user tries to change quantity of sold items
                    if (detailRequest.getQuantity() != existingDetail.getQuantity()) {
                        throw new BadRequestException("Không thể sửa số lượng nhập của thuốc '" + medicine.getMedicineName() + 
                            "' vì đã bán. Số lượng nhập ban đầu: " + existingDetail.getQuantity());
                    }
                    // Keep original totals for this item
                    totalQuantity += existingDetail.getQuantity();
                    totalValue = totalValue.add(BigDecimal.valueOf(existingDetail.getImportPrice()).multiply(BigDecimal.valueOf(existingDetail.getQuantity())));
                }
            } else {
                // New item - create both detail and inventory
                ImportDetail detail = InventoryMapper.toImportDetailEntity(detailRequest, medicineImport, medicine);
                importDetailRepository.save(detail);
                
                MedicineInventory inventory = InventoryMapper.toMedicineInventoryEntity(detailRequest, medicineImport, medicine);
                medicineInventoryRepository.save(inventory);
                
                totalQuantity += detailRequest.getQuantity();
                totalValue = totalValue.add(detailRequest.getImportPrice().multiply(BigDecimal.valueOf(detailRequest.getQuantity())));
            }
        }
        
        medicineImport.setTotalQuantity(totalQuantity);
        medicineImport.setTotalValue(totalValue);
        
        // Save updated import
        medicineImport = medicineImportRepository.save(medicineImport);
        
        // Return response with updated editable status
        List<ImportDetail> savedDetails = importDetailRepository.findByImportId(medicineImport.getImportId());
        List<MedicineInventory> savedInventories = medicineInventoryRepository.findByImportId(importId);
        
        Map<Integer, MedicineInventory> inventoryMap = new HashMap<>();
        for (MedicineInventory inv : savedInventories) {
            inventoryMap.put(inv.getMedicine().getMedicineId(), inv);
        }
        
        return InventoryMapper.toMedicineImportDetailResponse(medicineImport, savedDetails, inventoryMap);
    }

    @Override
    @Transactional
    public void deleteImport(int importId) {
        // Check if import exists
        MedicineImport medicineImport = medicineImportRepository.findById(importId)
            .orElseThrow(() -> new NotFoundException("Medicine import not found with id: " + importId));
        
        // Get existing details and inventories
        List<ImportDetail> existingDetails = importDetailRepository.findByImportId(importId);
        List<MedicineInventory> existingInventories = medicineInventoryRepository.findByImportId(importId);
        
        // Build inventory map
        Map<Integer, MedicineInventory> inventoryMap = new HashMap<>();
        for (MedicineInventory inv : existingInventories) {
            inventoryMap.put(inv.getMedicine().getMedicineId(), inv);
        }
        
        // Check if ALL items are editable (no items sold)
        for (ImportDetail detail : existingDetails) {
            MedicineInventory inv = inventoryMap.get(detail.getMedicine().getMedicineId());
            if (inv == null || inv.getQuantityInStock() != detail.getQuantity()) {
                int soldQuantity = detail.getQuantity() - (inv != null ? inv.getQuantityInStock() : 0);
                throw new BadRequestException("Không thể xóa phiếu nhập vì thuốc '" + detail.getMedicine().getMedicineName() + 
                    "' đã bán " + soldQuantity + " đơn vị. Chỉ có thể xóa phiếu nhập khi chưa bán thuốc nào.");
            }
        }
        
        // All items are editable, proceed with deletion
        // Delete inventory entries first (foreign key constraint)
        medicineInventoryRepository.deleteByImportId(importId);
        
        // Delete import details
        importDetailRepository.deleteByImportId(importId);
        
        // Delete import
        medicineImportRepository.deleteById(importId);
    }
}
