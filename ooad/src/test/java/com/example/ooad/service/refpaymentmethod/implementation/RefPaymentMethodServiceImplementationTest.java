package com.example.ooad.service.refpaymentmethod.implementation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.RefPaymentMethod;
import com.example.ooad.dto.request.RefPaymentMethodRequest;
import com.example.ooad.dto.response.RefPaymentMethodResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.RefPaymentMethodRepository;

@ExtendWith(MockitoExtension.class)
public class RefPaymentMethodServiceImplementationTest {

    @Mock
    private RefPaymentMethodRepository refPaymentMethodRepository;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RefPaymentMethodServiceImplementation paymentMethodService;

    private RefPaymentMethodRequest validRequest;
    private RefPaymentMethod savedEntity;
    private int paymentMethodId = 1;

    @BeforeEach
    void setUp() {
        validRequest = new RefPaymentMethodRequest();
        validRequest.setMethodCode("CASH");
        validRequest.setMethodName("Cash Payment");
        validRequest.setDescription("Pay with cash");
        validRequest.setIsActive(true);
        validRequest.setSortOrder(1);

        savedEntity = new RefPaymentMethod();
        savedEntity.setPaymentMethodId(1);
        savedEntity.setMethodCode("CASH");
        savedEntity.setMethodName("Cash Payment");
        savedEntity.setDescription("Pay with cash");
        savedEntity.setActive(true);
        savedEntity.setSortOrder(1);
    }

    // ==================== CREATE PAYMENT METHOD TESTS ====================

    @Test
    @DisplayName("Create Payment Method - Success: Should save and return response")
    void createPaymentMethod_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(refPaymentMethodRepository.existsByMethodCode("CASH")).thenReturn(false);
        when(refPaymentMethodRepository.save(any(RefPaymentMethod.class))).thenReturn(savedEntity);

        RefPaymentMethodResponse response = paymentMethodService.createPaymentMethod(validRequest, bindingResult);

        assertNotNull(response);
        assertEquals(savedEntity.getPaymentMethodId(), response.getPaymentMethodId());
        assertEquals(savedEntity.getMethodCode(), response.getMethodCode());
        assertEquals(savedEntity.getMethodName(), response.getMethodName());
        assertEquals(savedEntity.getDescription(), response.getDescription());
        assertTrue(response.isActive());

        verify(refPaymentMethodRepository, times(1)).existsByMethodCode("CASH");
        verify(refPaymentMethodRepository, times(1)).save(any(RefPaymentMethod.class));
    }

    @Test
    @DisplayName("Create Payment Method - Fail: Validation Error")
    void createPaymentMethod_ValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
            Arrays.asList(new org.springframework.validation.ObjectError("methodCode", "Method code is required"))
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            paymentMethodService.createPaymentMethod(validRequest, bindingResult);
        });

        assertEquals("Method code is required", exception.getMessage());
        verify(refPaymentMethodRepository, never()).save(any(RefPaymentMethod.class));
    }

    @Test
    @DisplayName("Create Payment Method - Fail: Method Code Already Exists")
    void createPaymentMethod_MethodCodeExists() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(refPaymentMethodRepository.existsByMethodCode("CASH")).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            paymentMethodService.createPaymentMethod(validRequest, bindingResult);
        });

        assertEquals("Payment method with code 'CASH' already exists", exception.getMessage());
        verify(refPaymentMethodRepository, never()).save(any(RefPaymentMethod.class));
    }

    // ==================== UPDATE PAYMENT METHOD TESTS ====================

    @Test
    @DisplayName("Update Payment Method - Success: Should update and return response")
    void updatePaymentMethod_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(refPaymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.of(savedEntity));
        when(refPaymentMethodRepository.save(any(RefPaymentMethod.class))).thenReturn(savedEntity);

        RefPaymentMethodRequest updateRequest = new RefPaymentMethodRequest();
        updateRequest.setMethodCode("CASH");
        updateRequest.setMethodName("Updated Cash Payment");
        updateRequest.setDescription("Updated description");
        updateRequest.setIsActive(true);
        updateRequest.setSortOrder(2);

        RefPaymentMethodResponse response = paymentMethodService.updatePaymentMethod(paymentMethodId, updateRequest, bindingResult);

        assertNotNull(response);
        verify(refPaymentMethodRepository, times(1)).findById(paymentMethodId);
        verify(refPaymentMethodRepository, times(1)).save(any(RefPaymentMethod.class));
    }

    @Test
    @DisplayName("Update Payment Method - Fail: Not Found")
    void updatePaymentMethod_NotFound() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(refPaymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            paymentMethodService.updatePaymentMethod(paymentMethodId, validRequest, bindingResult);
        });

        assertEquals("Payment method not found with id: " + paymentMethodId, exception.getMessage());
        verify(refPaymentMethodRepository, never()).save(any(RefPaymentMethod.class));
    }

    @Test
    @DisplayName("Update Payment Method - Fail: Method Code Already Exists (different entity)")
    void updatePaymentMethod_MethodCodeConflict() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(refPaymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.of(savedEntity));
        
        RefPaymentMethodRequest updateRequest = new RefPaymentMethodRequest();
        updateRequest.setMethodCode("CARD"); // Different code
        updateRequest.setMethodName("Card Payment");
        updateRequest.setIsActive(true);

        when(refPaymentMethodRepository.existsByMethodCode("CARD")).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            paymentMethodService.updatePaymentMethod(paymentMethodId, updateRequest, bindingResult);
        });

        assertEquals("Payment method with code 'CARD' already exists", exception.getMessage());
        verify(refPaymentMethodRepository, never()).save(any(RefPaymentMethod.class));
    }

    @Test
    @DisplayName("Update Payment Method - Fail: Validation Error")
    void updatePaymentMethod_ValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
            Arrays.asList(new org.springframework.validation.ObjectError("methodName", "Method name is required"))
        );

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            paymentMethodService.updatePaymentMethod(paymentMethodId, validRequest, bindingResult);
        });

        assertEquals("Method name is required", exception.getMessage());
        verify(refPaymentMethodRepository, never()).findById(paymentMethodId);
    }

    // ==================== GET PAYMENT METHOD BY ID TESTS ====================

    @Test
    @DisplayName("Get Payment Method By Id - Success")
    void getPaymentMethodById_Success() {
        when(refPaymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.of(savedEntity));

        RefPaymentMethodResponse response = paymentMethodService.getPaymentMethodById(paymentMethodId);

        assertNotNull(response);
        assertEquals(savedEntity.getPaymentMethodId(), response.getPaymentMethodId());
        assertEquals(savedEntity.getMethodCode(), response.getMethodCode());
        verify(refPaymentMethodRepository, times(1)).findById(paymentMethodId);
    }

    @Test
    @DisplayName("Get Payment Method By Id - Fail: Not Found")
    void getPaymentMethodById_NotFound() {
        when(refPaymentMethodRepository.findById(paymentMethodId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            paymentMethodService.getPaymentMethodById(paymentMethodId);
        });

        assertEquals("Payment method not found with id: " + paymentMethodId, exception.getMessage());
    }

    // ==================== GET ALL PAYMENT METHODS TESTS ====================

    @Test
    @DisplayName("Get All Payment Methods - Success")
    void getAllPaymentMethods_Success() {
        RefPaymentMethod secondEntity = new RefPaymentMethod();
        secondEntity.setPaymentMethodId(2);
        secondEntity.setMethodCode("CARD");
        secondEntity.setMethodName("Card Payment");
        secondEntity.setActive(true);

        when(refPaymentMethodRepository.findAll()).thenReturn(Arrays.asList(savedEntity, secondEntity));

        List<RefPaymentMethodResponse> responses = paymentMethodService.getAllPaymentMethods();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("CASH", responses.get(0).getMethodCode());
        assertEquals("CARD", responses.get(1).getMethodCode());
    }

    @Test
    @DisplayName("Get All Payment Methods - Empty List")
    void getAllPaymentMethods_Empty() {
        when(refPaymentMethodRepository.findAll()).thenReturn(Arrays.asList());

        List<RefPaymentMethodResponse> responses = paymentMethodService.getAllPaymentMethods();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    // ==================== GET ALL ACTIVE PAYMENT METHODS TESTS ====================

    @Test
    @DisplayName("Get All Active Payment Methods - Success")
    void getAllActivePaymentMethods_Success() {
        RefPaymentMethod inactiveEntity = new RefPaymentMethod();
        inactiveEntity.setPaymentMethodId(2);
        inactiveEntity.setMethodCode("CARD");
        inactiveEntity.setMethodName("Card Payment");
        inactiveEntity.setActive(false);

        when(refPaymentMethodRepository.findAll()).thenReturn(Arrays.asList(savedEntity, inactiveEntity));

        List<RefPaymentMethodResponse> responses = paymentMethodService.getAllActivePaymentMethods();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("CASH", responses.get(0).getMethodCode());
        assertTrue(responses.get(0).isActive());
    }

    @Test
    @DisplayName("Get All Active Payment Methods - No Active Methods")
    void getAllActivePaymentMethods_NoActive() {
        savedEntity.setActive(false);
        when(refPaymentMethodRepository.findAll()).thenReturn(Arrays.asList(savedEntity));

        List<RefPaymentMethodResponse> responses = paymentMethodService.getAllActivePaymentMethods();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    // ==================== DELETE PAYMENT METHOD TESTS ====================

    @Test
    @DisplayName("Delete Payment Method - Success")
    void deletePaymentMethod_Success() {
        when(refPaymentMethodRepository.existsById(paymentMethodId)).thenReturn(true);

        paymentMethodService.deletePaymentMethod(paymentMethodId);

        verify(refPaymentMethodRepository, times(1)).existsById(paymentMethodId);
        verify(refPaymentMethodRepository, times(1)).deleteById(paymentMethodId);
    }

    @Test
    @DisplayName("Delete Payment Method - Fail: Not Found")
    void deletePaymentMethod_NotFound() {
        when(refPaymentMethodRepository.existsById(paymentMethodId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            paymentMethodService.deletePaymentMethod(paymentMethodId);
        });

        assertEquals("Payment method not found with id: " + paymentMethodId, exception.getMessage());
        verify(refPaymentMethodRepository, never()).deleteById(paymentMethodId);
    }
}
