package com.example.ooad.service.inventory.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.MedicineImport;
import com.example.ooad.dto.response.inventory.MedicineImportListResponse;
import com.example.ooad.dto.response.inventory.WarehouseDashboardResponse;
import com.example.ooad.mapper.InventoryMapper;
import com.example.ooad.repository.MedicineImportRepository;
import com.example.ooad.repository.MedicineInventoryRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.service.inventory.interfaces.WarehouseDashboardService;

@Service
public class WarehouseDashboardServiceImplementation implements WarehouseDashboardService {

    private final MedicineRepository medicineRepository;
    private final MedicineInventoryRepository medicineInventoryRepository;
    private final MedicineImportRepository medicineImportRepository;

    public WarehouseDashboardServiceImplementation(MedicineRepository medicineRepository,
            MedicineInventoryRepository medicineInventoryRepository,
            MedicineImportRepository medicineImportRepository) {
        this.medicineRepository = medicineRepository;
        this.medicineInventoryRepository = medicineInventoryRepository;
        this.medicineImportRepository = medicineImportRepository;
    }

    @Override
    public WarehouseDashboardResponse getDashboardStats() {
        WarehouseDashboardResponse response = new WarehouseDashboardResponse();

        // 1. Total Medicines
        requestTotalMedicines(response);

        // 2. Low Stock (Threshold: 10 units? Or maybe just count out of stock ie. 0.
        // InventoryFilterRequest has includeOutOfStock)
        // Let's assume low stock is <= 10.
        requestLowStock(response);

        // 3. Expiring Soon (Next 30 days)
        requestExpiringSoon(response);

        // 4. Import Value this Month
        requestImportValue(response);

        // 5. Recent Imports
        requestRecentImports(response);

        return response;
    }

    private void requestTotalMedicines(WarehouseDashboardResponse response) {
        long count = medicineRepository.count();
        response.setTotalMedicines(count);
    }

    private void requestLowStock(WarehouseDashboardResponse response) {
        // Threshold for low stock is implicitly what we define here. E.g., 50 units.
        long count = medicineInventoryRepository.countLowStockMedicines(50);
        response.setTotalOutOfStock(count);
    }

    private void requestExpiringSoon(WarehouseDashboardResponse response) {
        // Expiring in next 30 days
        LocalDate now = LocalDate.now();
        LocalDate next30Days = now.plusDays(30);
        long count = medicineInventoryRepository.countExpiringBatches(Date.valueOf(next30Days));
        response.setTotalExpiringSoon(count);
    }

    private void requestImportValue(WarehouseDashboardResponse response) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        BigDecimal val = medicineImportRepository.sumTotalValueByDateRange(Date.valueOf(startOfMonth),
                Date.valueOf(endOfMonth));
        response.setTotalImportValueMonth(val);
    }

    private void requestRecentImports(WarehouseDashboardResponse response) {
        List<MedicineImport> imports = medicineImportRepository.findTop5ByOrderByImportDateDesc();
        List<MedicineImportListResponse> list = imports.stream()
                .map(InventoryMapper::toMedicineImportListResponse)
                .collect(Collectors.toList());
        response.setRecentImports(list);
    }
}
