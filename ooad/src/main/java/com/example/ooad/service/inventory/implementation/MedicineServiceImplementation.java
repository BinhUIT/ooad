package com.example.ooad.service.inventory.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
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

import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.MedicineInventory;
import com.example.ooad.domain.entity.MedicinePrice;
import com.example.ooad.dto.request.inventory.MedicineFilterRequest;
import com.example.ooad.dto.request.inventory.MedicineRequest;
import com.example.ooad.dto.response.inventory.MedicineDetailResponse;
import com.example.ooad.dto.response.inventory.MedicineInventoryResponse;
import com.example.ooad.dto.response.inventory.MedicinePriceResponse;
import com.example.ooad.dto.response.inventory.MedicineResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.MedicineInventoryRepository;
import com.example.ooad.repository.MedicinePriceRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.repository.PrescriptionDetailRepository;
import com.example.ooad.service.inventory.interfaces.MedicineService;

@Service
public class MedicineServiceImplementation implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineInventoryRepository medicineInventoryRepository;
    private final MedicinePriceRepository medicinePriceRepository;
    private final PrescriptionDetailRepository prescriptionDetailRepository;

    public MedicineServiceImplementation(
            MedicineRepository medicineRepository,
            MedicineInventoryRepository medicineInventoryRepository,
            MedicinePriceRepository medicinePriceRepository,
            PrescriptionDetailRepository prescriptionDetailRepository) {
        this.medicineRepository = medicineRepository;
        this.medicineInventoryRepository = medicineInventoryRepository;
        this.medicinePriceRepository = medicinePriceRepository;
        this.prescriptionDetailRepository = prescriptionDetailRepository;
    }

    @Override
    public Page<MedicineResponse> getMedicineList(MedicineFilterRequest filter) {
        Sort sort = Sort.by(filter.getSortType().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC,
                filter.getSortBy());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        Specification<Medicine> spec = Specification.where(null);

        // Filter by keyword
        if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("medicineName")),
                    "%" + filter.getKeyword().toLowerCase() + "%"));
        }

        // Filter by unit
        if (filter.getUnit() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("unit"), filter.getUnit()));
        }

        // Filter by manufacturer
        if (filter.getManufacturer() != null && !filter.getManufacturer().trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("manufacturer")),
                    "%" + filter.getManufacturer().toLowerCase() + "%"));
        }

        Page<Medicine> medicinePage = medicineRepository.findAll(spec, pageable);

        return medicinePage.map(this::toMedicineResponse);
    }

    @Override
    public List<MedicineResponse> getAllMedicines() {
        return medicineRepository.findAll().stream()
                .map(this::toMedicineResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MedicineDetailResponse getMedicineDetail(int medicineId, boolean includeZeroQuantity) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NotFoundException("Medicine not found with ID: " + medicineId));

        MedicineDetailResponse response = new MedicineDetailResponse();
        response.setMedicine(toMedicineResponse(medicine));

        // Get current price
        Date today = Date.valueOf(LocalDate.now());
        BigDecimal currentPrice = medicinePriceRepository.getCurrentPrice(medicineId, today);
        response.setCurrentPrice(currentPrice);

        // Get inventory list
        List<MedicineInventory> inventories = medicineInventoryRepository.findByMedicineId(medicineId);
        List<MedicineInventoryResponse> inventoryResponses = inventories.stream()
                .filter(inv -> includeZeroQuantity || inv.getQuantityInStock() > 0)
                .map(this::toInventoryResponse)
                .collect(Collectors.toList());
        response.setInventories(inventoryResponses);

        // Get price history
        List<MedicinePrice> prices = medicinePriceRepository.findByMedicineIdOrderByEffectiveDateDesc(medicineId);
        List<MedicinePriceResponse> priceResponses = prices.stream()
                .map(price -> toPriceResponse(price, today))
                .collect(Collectors.toList());
        response.setPriceHistory(priceResponses);

        return response;
    }

    @Override
    @Transactional
    public MedicineResponse createMedicine(MedicineRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    "Validation failed: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        // Check if medicine name already exists
        if (medicineRepository.existsByMedicineNameIgnoreCase(request.getMedicineName())) {
            throw new BadRequestException("Medicine name already exists: " + request.getMedicineName());
        }

        Medicine medicine = new Medicine();
        medicine.setMedicineName(request.getMedicineName());
        medicine.setUnit(request.getUnit());
        medicine.setConcentration(request.getConcentration());
        medicine.setForm(request.getForm());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setUsageInstructions(request.getUsageInstructions());
        medicine.setImage(request.getImage());
        medicine.setStorageCondition(request.getStorageCondition());

        Medicine saved = medicineRepository.save(medicine);
        return toMedicineResponse(saved);
    }

    @Override
    @Transactional
    public MedicineResponse updateMedicine(int medicineId, MedicineRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    "Validation failed: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NotFoundException("Medicine not found with ID: " + medicineId));

        // Check if new name already exists (excluding current medicine)
        Medicine existingMedicine = medicineRepository.findAll().stream()
                .filter(m -> m.getMedicineName().equalsIgnoreCase(request.getMedicineName())
                        && m.getMedicineId() != medicineId)
                .findFirst()
                .orElse(null);

        if (existingMedicine != null) {
            throw new BadRequestException("Medicine name already exists: " + request.getMedicineName());
        }

        medicine.setMedicineName(request.getMedicineName());
        medicine.setUnit(request.getUnit());
        medicine.setConcentration(request.getConcentration());
        medicine.setForm(request.getForm());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setUsageInstructions(request.getUsageInstructions());
        medicine.setImage(request.getImage());
        if (request.getStorageCondition() != null) {
            medicine.setStorageCondition(request.getStorageCondition());
        }

        Medicine updated = medicineRepository.save(medicine);
        return toMedicineResponse(updated);
    }

    @Override
    @Transactional
    public void deleteMedicine(int medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NotFoundException("Medicine not found with ID: " + medicineId));

        // Check if medicine is used in inventory
        int totalInventory = medicineInventoryRepository.getTotalQuantityByMedicineId(medicineId);
        if (totalInventory > 0) {
            throw new BadRequestException("Cannot delete medicine that has inventory records");
        }

        // Check if medicine is used in prescriptions
        boolean usedInPrescription = prescriptionDetailRepository.existsByMedicine_MedicineId(medicineId);
        if (usedInPrescription) {
            throw new BadRequestException("Cannot delete medicine that is used in prescriptions");
        }

        medicineRepository.delete(medicine);
    }

    @Override
    public List<String> getManufacturers() {
        return medicineRepository.findDistinctManufacturers();
    }

    // Helper methods
    private MedicineResponse toMedicineResponse(Medicine medicine) {
        MedicineResponse response = new MedicineResponse();
        response.setMedicineId(medicine.getMedicineId());
        response.setMedicineName(medicine.getMedicineName());
        response.setUnit(medicine.getUnit());
        response.setConcentration(medicine.getConcentration());
        response.setForm(medicine.getForm());
        response.setManufacturer(medicine.getManufacturer());
        response.setUsageInstructions(medicine.getUsageInstructions());
        response.setImage(medicine.getImage());
        response.setStorageCondition(medicine.getStorageCondition());

        // Get total quantity
        int totalQuantity = medicineInventoryRepository.getTotalQuantityByMedicineId(medicine.getMedicineId());
        response.setTotalQuantity(totalQuantity);

        return response;
    }

    private MedicineInventoryResponse toInventoryResponse(MedicineInventory inventory) {
        MedicineInventoryResponse response = new MedicineInventoryResponse();
        response.setImportId(inventory.getMedicineImport().getImportId());
        response.setImportDate(inventory.getMedicineImport().getImportDate());
        response.setExpiryDate(inventory.getExpiryDate());
        response.setImportPrice(inventory.getImportPrice());
        response.setQuantityInStock(inventory.getQuantityInStock());
        response.setSupplier(inventory.getMedicineImport().getSupplier());
        return response;
    }

    private MedicinePriceResponse toPriceResponse(MedicinePrice price, Date today) {
        boolean canDelete = price.getMedicinePriceId().getEffectiveDate().equals(today);
        return new MedicinePriceResponse(
                price.getMedicine().getMedicineId(),
                price.getMedicinePriceId().getEffectiveDate(),
                price.getUnitPrice(),
                canDelete);
    }
}
