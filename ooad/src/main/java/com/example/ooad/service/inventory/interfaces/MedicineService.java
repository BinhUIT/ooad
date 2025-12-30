package com.example.ooad.service.inventory.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.example.ooad.dto.request.inventory.MedicineFilterRequest;
import com.example.ooad.dto.request.inventory.MedicineRequest;
import com.example.ooad.dto.response.inventory.MedicineDetailResponse;
import com.example.ooad.dto.response.inventory.MedicineResponse;

public interface MedicineService {

    Page<MedicineResponse> getMedicineList(MedicineFilterRequest filter);

    List<MedicineResponse> getAllMedicines();

    MedicineDetailResponse getMedicineDetail(int medicineId, boolean includeZeroQuantity);

    MedicineResponse createMedicine(MedicineRequest request, BindingResult bindingResult);

    MedicineResponse updateMedicine(int medicineId, MedicineRequest request, BindingResult bindingResult);

    void deleteMedicine(int medicineId);

    List<String> getManufacturers();
}
