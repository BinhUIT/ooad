package com.example.ooad.service.inventory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.compositekey.MedicinePriceKey;
import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.entity.MedicinePrice;
import com.example.ooad.domain.enums.EMedicineUnit;
import com.example.ooad.dto.request.inventory.MedicinePriceRequest;
import com.example.ooad.dto.response.inventory.MedicinePriceResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.MedicinePriceRepository;
import com.example.ooad.repository.MedicineRepository;
import com.example.ooad.service.inventory.implementation.MedicinePriceServiceImplementation;

@ExtendWith(MockitoExtension.class)
class MedicinePriceServiceTest {

    @Mock
    private MedicinePriceRepository medicinePriceRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private MedicinePriceServiceImplementation medicinePriceService;

    private Medicine testMedicine;
    private MedicinePrice testPrice;
    private MedicinePriceRequest testRequest;

    @BeforeEach
    void setUp() {
        testMedicine = new Medicine();
        testMedicine.setMedicineId(1);
        testMedicine.setMedicineName("Paracetamol");
        testMedicine.setUnit(EMedicineUnit.TABLET);

        Date futureDate = Date.valueOf(LocalDate.now().plusDays(7));
        MedicinePriceKey key = new MedicinePriceKey(1, futureDate);

        testPrice = new MedicinePrice();
        testPrice.setMedicinePriceId(key);
        testPrice.setMedicine(testMedicine);
        testPrice.setUnitPrice(new BigDecimal("5000"));

        testRequest = new MedicinePriceRequest();
        testRequest.setEffectiveDate(futureDate);
        testRequest.setUnitPrice(new BigDecimal("5000"));
    }

    @Test
    void testGetPriceHistory_Success() {
        // Arrange
        int medicineId = 1;
        Date today = Date.valueOf(LocalDate.now());
        Date pastDate = Date.valueOf(LocalDate.now().minusDays(30));
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(7));

        MedicinePriceKey pastKey = new MedicinePriceKey(medicineId, pastDate);
        MedicinePrice pastPrice = new MedicinePrice();
        pastPrice.setMedicinePriceId(pastKey);
        pastPrice.setMedicine(testMedicine);
        pastPrice.setUnitPrice(new BigDecimal("4000"));

        MedicinePriceKey futureKey = new MedicinePriceKey(medicineId, futureDate);
        MedicinePrice futurePrice = new MedicinePrice();
        futurePrice.setMedicinePriceId(futureKey);
        futurePrice.setMedicine(testMedicine);
        futurePrice.setUnitPrice(new BigDecimal("6000"));

        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.findByMedicineIdOrderByEffectiveDateDesc(medicineId))
                .thenReturn(Arrays.asList(futurePrice, pastPrice));

        // Act
        List<MedicinePriceResponse> result = medicinePriceService.getPriceHistory(medicineId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).isCanDelete()); // Future price can be deleted
        assertFalse(result.get(1).isCanDelete()); // Past price cannot be deleted
    }

    @Test
    void testGetPriceHistory_MedicineNotFound() {
        // Arrange
        int medicineId = 999;
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            medicinePriceService.getPriceHistory(medicineId);
        });
    }

    @Test
    void testAddPrice_Success() {
        // Arrange
        int medicineId = 1;
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(7));
        testRequest.setEffectiveDate(futureDate);
        testRequest.setUnitPrice(new BigDecimal("6000"));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.getMaxImportPrice(medicineId))
                .thenReturn(new BigDecimal("5000"));
        when(medicinePriceRepository.existsById(any())).thenReturn(false);
        when(medicinePriceRepository.save(any(MedicinePrice.class))).thenReturn(testPrice);

        // Act
        MedicinePriceResponse result = medicinePriceService.addPrice(medicineId, testRequest, bindingResult);

        // Assert
        assertNotNull(result);
        assertEquals(medicineId, result.getMedicineId());
        verify(medicinePriceRepository).save(any(MedicinePrice.class));
    }

    @Test
    void testAddPrice_ValidationError() {
        // Arrange
        int medicineId = 1;
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(
                new org.springframework.validation.ObjectError("priceRequest", "Price is required")));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            medicinePriceService.addPrice(medicineId, testRequest, bindingResult);
        });
    }

    @Test
    void testAddPrice_MedicineNotFound() {
        // Arrange
        int medicineId = 999;
        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            medicinePriceService.addPrice(medicineId, testRequest, bindingResult);
        });
    }

    @Test
    void testAddPrice_PriceTooLow() {
        // Arrange
        int medicineId = 1;
        testRequest.setUnitPrice(new BigDecimal("3000")); // Lower than import price

        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.getMaxImportPrice(medicineId))
                .thenReturn(new BigDecimal("5000")); // Import price is higher

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            medicinePriceService.addPrice(medicineId, testRequest, bindingResult);
        });
        assertTrue(exception.getMessage().contains("greater than import price"));
    }

    @Test
    void testAddPrice_DuplicateDate() {
        // Arrange
        int medicineId = 1;
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(7));
        testRequest.setEffectiveDate(futureDate);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.getMaxImportPrice(medicineId))
                .thenReturn(new BigDecimal("4000"));
        when(medicinePriceRepository.existsById(any())).thenReturn(true); // Price already exists

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            medicinePriceService.addPrice(medicineId, testRequest, bindingResult);
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testUpdatePrice_Success() {
        // Arrange
        int medicineId = 1;
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(7));

        MedicinePriceRequest oldPrice = new MedicinePriceRequest();
        oldPrice.setEffectiveDate(futureDate);
        oldPrice.setUnitPrice(new BigDecimal("5000"));

        MedicinePriceRequest newPrice = new MedicinePriceRequest();
        newPrice.setEffectiveDate(Date.valueOf(LocalDate.now().plusDays(10)));
        newPrice.setUnitPrice(new BigDecimal("6000"));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.getMaxImportPrice(medicineId))
                .thenReturn(new BigDecimal("4000"));
        when(medicinePriceRepository.existsById(any())).thenReturn(false);
        when(medicinePriceRepository.save(any(MedicinePrice.class))).thenReturn(testPrice);

        // Act
        MedicinePriceResponse result = medicinePriceService.updatePrice(
                medicineId, oldPrice, newPrice, bindingResult);

        // Assert
        assertNotNull(result);
        verify(medicinePriceRepository).deleteById(any(MedicinePriceKey.class));
        verify(medicinePriceRepository).save(any(MedicinePrice.class));
    }

    @Test
    void testUpdatePrice_PastDate() {
        // Arrange
        int medicineId = 1;
        Date pastDate = Date.valueOf(LocalDate.now().minusDays(7));

        MedicinePriceRequest oldPrice = new MedicinePriceRequest();
        oldPrice.setEffectiveDate(pastDate); // Past date - should not be allowed
        oldPrice.setUnitPrice(new BigDecimal("5000"));

        MedicinePriceRequest newPrice = new MedicinePriceRequest();
        newPrice.setEffectiveDate(Date.valueOf(LocalDate.now().plusDays(7)));
        newPrice.setUnitPrice(new BigDecimal("6000"));

        when(bindingResult.hasErrors()).thenReturn(false);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            medicinePriceService.updatePrice(medicineId, oldPrice, newPrice, bindingResult);
        });
        assertTrue(exception.getMessage().contains("future effective date"));
    }

    @Test
    void testUpdatePrice_NewPriceTooLow() {
        // Arrange
        int medicineId = 1;
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(7));

        MedicinePriceRequest oldPrice = new MedicinePriceRequest();
        oldPrice.setEffectiveDate(futureDate);
        oldPrice.setUnitPrice(new BigDecimal("5000"));

        MedicinePriceRequest newPrice = new MedicinePriceRequest();
        newPrice.setEffectiveDate(Date.valueOf(LocalDate.now().plusDays(10)));
        newPrice.setUnitPrice(new BigDecimal("3000")); // Too low

        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.getMaxImportPrice(medicineId))
                .thenReturn(new BigDecimal("5000")); // Import price is higher

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            medicinePriceService.updatePrice(medicineId, oldPrice, newPrice, bindingResult);
        });
        assertTrue(exception.getMessage().contains("greater than import price"));
    }

    @Test
    void testUpdatePrice_DuplicateNewDate() {
        // Arrange
        int medicineId = 1;
        Date futureDate1 = Date.valueOf(LocalDate.now().plusDays(7));
        Date futureDate2 = Date.valueOf(LocalDate.now().plusDays(10));

        MedicinePriceRequest oldPrice = new MedicinePriceRequest();
        oldPrice.setEffectiveDate(futureDate1);
        oldPrice.setUnitPrice(new BigDecimal("5000"));

        MedicinePriceRequest newPrice = new MedicinePriceRequest();
        newPrice.setEffectiveDate(futureDate2); // This date already has a price
        newPrice.setUnitPrice(new BigDecimal("6000"));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.getMaxImportPrice(medicineId))
                .thenReturn(new BigDecimal("4000"));
        when(medicinePriceRepository.existsById(any())).thenReturn(true); // New date already exists

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            medicinePriceService.updatePrice(medicineId, oldPrice, newPrice, bindingResult);
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testDeletePrice_Success() {
        // Arrange
        int medicineId = 1;
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(7));
        MedicinePriceRequest request = new MedicinePriceRequest();
        request.setEffectiveDate(futureDate);

        MedicinePriceKey key = new MedicinePriceKey(medicineId, futureDate);

        when(medicinePriceRepository.existsById(key)).thenReturn(true);
        doNothing().when(medicinePriceRepository).deleteById(key);

        // Act
        medicinePriceService.deletePrice(medicineId, request);

        // Assert
        verify(medicinePriceRepository).deleteById(key);
        verify(medicinePriceRepository).deleteById(key);
    }

    @Test
    void testAddPrice_NoImportPriceRestriction() {
        // Arrange - medicine with no import price yet
        int medicineId = 1;
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(7));
        testRequest.setEffectiveDate(futureDate);
        testRequest.setUnitPrice(new BigDecimal("5000"));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.getMaxImportPrice(medicineId)).thenReturn(null); // No import price
        when(medicinePriceRepository.existsById(any())).thenReturn(false);
        when(medicinePriceRepository.save(any(MedicinePrice.class))).thenReturn(testPrice);

        // Act
        MedicinePriceResponse result = medicinePriceService.addPrice(medicineId, testRequest, bindingResult);

        // Assert
        assertNotNull(result);
        verify(medicinePriceRepository).save(any(MedicinePrice.class));
    }

    @Test
    void testGetPriceHistory_EmptyList() {
        // Arrange
        int medicineId = 1;
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.findByMedicineIdOrderByEffectiveDateDesc(medicineId))
                .thenReturn(Arrays.asList());

        // Act
        List<MedicinePriceResponse> result = medicinePriceService.getPriceHistory(medicineId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testUpdatePrice_SameDate() {
        // Arrange - updating price with same date
        int medicineId = 1;
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(7));

        MedicinePriceRequest oldPrice = new MedicinePriceRequest();
        oldPrice.setEffectiveDate(futureDate);
        oldPrice.setUnitPrice(new BigDecimal("5000"));

        MedicinePriceRequest newPrice = new MedicinePriceRequest();
        newPrice.setEffectiveDate(futureDate); // Same date
        newPrice.setUnitPrice(new BigDecimal("6000")); // Different price

        when(bindingResult.hasErrors()).thenReturn(false);
        when(medicineRepository.findById(medicineId)).thenReturn(Optional.of(testMedicine));
        when(medicinePriceRepository.getMaxImportPrice(medicineId))
                .thenReturn(new BigDecimal("4000"));
        when(medicinePriceRepository.save(any(MedicinePrice.class))).thenReturn(testPrice);

        // Act
        MedicinePriceResponse result = medicinePriceService.updatePrice(
                medicineId, oldPrice, newPrice, bindingResult);

        // Assert
        assertNotNull(result);
        verify(medicinePriceRepository).deleteById(any(MedicinePriceKey.class));
        verify(medicinePriceRepository).save(any(MedicinePrice.class));
    }
}
