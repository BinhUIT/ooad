package com.example.ooad.service.inventory.interfaces;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.example.ooad.dto.request.inventory.MedicinePriceRequest;
import com.example.ooad.dto.response.inventory.MedicinePriceResponse;

public interface MedicinePriceService {

    List<MedicinePriceResponse> getPriceHistory(int medicineId);

    MedicinePriceResponse addPrice(int medicineId, MedicinePriceRequest request, BindingResult bindingResult);

    MedicinePriceResponse updatePrice(int medicineId, MedicinePriceRequest oldPrice, MedicinePriceRequest newPrice,
            BindingResult bindingResult);

    void deletePrice(int medicineId, MedicinePriceRequest request);
}
