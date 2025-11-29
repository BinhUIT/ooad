package com.example.ooad.controller.refpaymentmethod;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.request.RefPaymentMethodFilterRequest;
import com.example.ooad.dto.request.RefPaymentMethodRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.RefPaymentMethodResponse;
import com.example.ooad.service.refpaymentmethod.interfaces.RefPaymentMethodService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Payment Method Management")
public class RefPaymentMethodController {
    
    private final RefPaymentMethodService refPaymentMethodService;

    public RefPaymentMethodController(RefPaymentMethodService refPaymentMethodService) {
        this.refPaymentMethodService = refPaymentMethodService;
    }

    @PostMapping("/admin/payment-methods")
    public ResponseEntity<GlobalResponse<RefPaymentMethodResponse>> createPaymentMethod(
            @RequestBody @Valid RefPaymentMethodRequest request,
            BindingResult bindingResult) {
        
        RefPaymentMethodResponse result = refPaymentMethodService.createPaymentMethod(request, bindingResult);
        GlobalResponse<RefPaymentMethodResponse> response = new GlobalResponse<>(result, Message.success, 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/admin/payment-methods/{id}")
    public ResponseEntity<GlobalResponse<RefPaymentMethodResponse>> updatePaymentMethod(
            @PathVariable int id,
            @RequestBody @Valid RefPaymentMethodRequest request,
            BindingResult bindingResult) {
        
        RefPaymentMethodResponse result = refPaymentMethodService.updatePaymentMethod(id, request, bindingResult);
        GlobalResponse<RefPaymentMethodResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/payment-methods/{id}")
    public ResponseEntity<GlobalResponse<RefPaymentMethodResponse>> getPaymentMethodById(@PathVariable int id) {
        RefPaymentMethodResponse result = refPaymentMethodService.getPaymentMethodById(id);
        GlobalResponse<RefPaymentMethodResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<GlobalResponse<List<RefPaymentMethodResponse>>> getAllPaymentMethods() {
        List<RefPaymentMethodResponse> result = refPaymentMethodService.getAllPaymentMethods();
        GlobalResponse<List<RefPaymentMethodResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/payment-methods/active")
    public ResponseEntity<GlobalResponse<List<RefPaymentMethodResponse>>> getAllActivePaymentMethods() {
        List<RefPaymentMethodResponse> result = refPaymentMethodService.getAllActivePaymentMethods();
        GlobalResponse<List<RefPaymentMethodResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/payment-methods")
    public ResponseEntity<GlobalResponse<Page<RefPaymentMethodResponse>>> searchPaymentMethods(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isActive
    ) {
        RefPaymentMethodFilterRequest filter = new RefPaymentMethodFilterRequest();
        if (page != null) filter.setPage(page);
        if (size != null) filter.setSize(size);
        if (sortBy != null) filter.setSortBy(sortBy);
        if (sortType != null) filter.setSortType(sortType);
        if (keyword != null) filter.setKeyword(keyword);
        if (isActive != null) filter.setIsActive(isActive);

        Page<RefPaymentMethodResponse> result = refPaymentMethodService.searchPaymentMethods(filter);
        GlobalResponse<Page<RefPaymentMethodResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/admin/payment-methods/{id}")
    public ResponseEntity<GlobalResponse<Void>> deletePaymentMethod(@PathVariable int id) {
        refPaymentMethodService.deletePaymentMethod(id);
        GlobalResponse<Void> response = new GlobalResponse<>(null, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
