package com.example.ooad.service.inventory.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.compositekey.MedicinePriceKey;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.MedicinePrice;
import com.example.ooad.dto.request.inventory.MedicinePriceRequest;
import com.example.ooad.dto.response.inventory.MedicinePriceResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.MedicinePriceRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.service.inventory.interfaces.MedicinePriceService;

@Service
public class MedicinePriceServiceImplementation implements MedicinePriceService {

    private final MedicinePriceRepository medicinePriceRepository;
    private final MedicineRepository medicineRepository;

    public MedicinePriceServiceImplementation(
            MedicinePriceRepository medicinePriceRepository,
            MedicineRepository medicineRepository) {
        this.medicinePriceRepository = medicinePriceRepository;
        this.medicineRepository = medicineRepository;
    }

    @Override
    public List<MedicinePriceResponse> getPriceHistory(int medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NotFoundException("Medicine not found with ID: " + medicineId));

        Date today = Date.valueOf(LocalDate.now());
        List<MedicinePrice> prices = medicinePriceRepository.findByMedicineIdOrderByEffectiveDateDesc(medicineId);

        return prices.stream()
                .map(price -> {
                    // Can delete/edit if effective date is in the future (not yet effective)
                    boolean canDelete = price.getMedicinePriceId().getEffectiveDate().after(today);
                    return new MedicinePriceResponse(
                            medicine.getMedicineId(),
                            price.getMedicinePriceId().getEffectiveDate(),
                            price.getUnitPrice(),
                            canDelete);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicinePriceResponse addPrice(int medicineId, MedicinePriceRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    "Validation failed: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NotFoundException("Medicine not found with ID: " + medicineId));

        // Validate: selling price must be greater than import price
        BigDecimal maxImportPrice = medicinePriceRepository.getMaxImportPrice(medicineId);
        if (maxImportPrice != null && request.getUnitPrice().compareTo(maxImportPrice) <= 0) {
            throw new BadRequestException("Selling price must be greater than import price (" + maxImportPrice + ")");
        }

        // Check if price already exists for this date
        MedicinePriceKey key = new MedicinePriceKey(medicineId, request.getEffectiveDate());
        if (medicinePriceRepository.existsById(key)) {
            throw new BadRequestException("Price already exists for date: " + request.getEffectiveDate());
        }

        MedicinePrice price = new MedicinePrice();
        price.setMedicinePriceId(key);
        price.setMedicine(medicine);
        price.setUnitPrice(request.getUnitPrice());

        MedicinePrice saved = medicinePriceRepository.save(price);

        Date today = Date.valueOf(LocalDate.now());
        boolean canDelete = saved.getMedicinePriceId().getEffectiveDate().after(today);

        return new MedicinePriceResponse(
                medicineId,
                saved.getMedicinePriceId().getEffectiveDate(),
                saved.getUnitPrice(),
                canDelete);
    }

    @Override
    @Transactional
    public MedicinePriceResponse updatePrice(int medicineId, MedicinePriceRequest oldPrice,
            MedicinePriceRequest newPrice, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    "Validation failed: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Date today = Date.valueOf(LocalDate.now());

        // Can only update prices that haven't become effective yet
        if (!oldPrice.getEffectiveDate().after(today)) {
            throw new BadRequestException("Can only update prices with future effective date (not yet effective)");
        }

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NotFoundException("Medicine not found with ID: " + medicineId));

        // Validate: selling price must be greater than import price
        BigDecimal maxImportPrice = medicinePriceRepository.getMaxImportPrice(medicineId);
        if (maxImportPrice != null && newPrice.getUnitPrice().compareTo(maxImportPrice) <= 0) {
            throw new BadRequestException("Selling price must be greater than import price (" + maxImportPrice + ")");
        }

        // Delete old price
        MedicinePriceKey oldKey = new MedicinePriceKey(medicineId, oldPrice.getEffectiveDate());
        medicinePriceRepository.deleteById(oldKey);

        // Add new price
        MedicinePriceKey newKey = new MedicinePriceKey(medicineId, newPrice.getEffectiveDate());
        if (medicinePriceRepository.existsById(newKey)
                && !oldPrice.getEffectiveDate().equals(newPrice.getEffectiveDate())) {
            throw new BadRequestException("Price already exists for date: " + newPrice.getEffectiveDate());
        }

        MedicinePrice price = new MedicinePrice();
        price.setMedicinePriceId(newKey);
        price.setMedicine(medicine);
        price.setUnitPrice(newPrice.getUnitPrice());

        MedicinePrice saved = medicinePriceRepository.save(price);

        boolean canDelete = saved.getMedicinePriceId().getEffectiveDate().after(today);

        return new MedicinePriceResponse(
                medicineId,
                saved.getMedicinePriceId().getEffectiveDate(),
                saved.getUnitPrice(),
                canDelete);
    }

    @Override
    @Transactional
    public void deletePrice(int medicineId, MedicinePriceRequest request) {
        Date today = Date.valueOf(LocalDate.now());

        // Can only delete prices that haven't become effective yet
        if (!request.getEffectiveDate().after(today)) {
            throw new BadRequestException("Can only delete prices with future effective date (not yet effective)");
        }

        MedicinePriceKey key = new MedicinePriceKey(medicineId, request.getEffectiveDate());
        if (!medicinePriceRepository.existsById(key)) {
            throw new NotFoundException("Price not found for date: " + request.getEffectiveDate());
        }

        medicinePriceRepository.deleteById(key);
    }
}
