package com.example.ooad.service.refpaymentmethod.interfaces;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.example.ooad.dto.request.RefPaymentMethodRequest;
import com.example.ooad.dto.response.RefPaymentMethodResponse;

public interface RefPaymentMethodService {
    RefPaymentMethodResponse createPaymentMethod(RefPaymentMethodRequest request, BindingResult bindingResult);
    RefPaymentMethodResponse updatePaymentMethod(int id, RefPaymentMethodRequest request, BindingResult bindingResult);
    RefPaymentMethodResponse getPaymentMethodById(int id);
    List<RefPaymentMethodResponse> getAllPaymentMethods();
    List<RefPaymentMethodResponse> getAllActivePaymentMethods();
    void deletePaymentMethod(int id);
}
