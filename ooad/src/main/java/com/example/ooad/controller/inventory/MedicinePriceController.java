package com.example.ooad.controller.inventory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.request.inventory.MedicinePriceRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.inventory.MedicinePriceResponse;
import com.example.ooad.service.inventory.interfaces.MedicinePriceService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/warehouse")
@Tag(name = "Medicine Price Management", description = "APIs for warehouse staff to manage medicine prices")
public class MedicinePriceController {

    private final MedicinePriceService medicinePriceService;

    public MedicinePriceController(MedicinePriceService medicinePriceService) {
        this.medicinePriceService = medicinePriceService;
    }

    /**
     * Get price history for a medicine
     * Endpoint: GET /warehouse/medicines/{medicineId}/prices
     */
    @GetMapping("/medicines/{medicineId}/prices")
    @Operation(summary = "Get price history", description = "Get all price history for a medicine, ordered by effective date (newest first)")
    public ResponseEntity<GlobalResponse<List<MedicinePriceResponse>>> getPriceHistory(
            @PathVariable int medicineId) {
        List<MedicinePriceResponse> result = medicinePriceService.getPriceHistory(medicineId);
        GlobalResponse<List<MedicinePriceResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Add new price for a medicine
     * Endpoint: POST /warehouse/medicines/{medicineId}/prices
     * Note: Selling price must be greater than import price
     */
    @PostMapping("/medicines/{medicineId}/prices")
    @Operation(summary = "Add new price", description = "Add new selling price for a medicine. Price must be greater than import price.")
    public ResponseEntity<GlobalResponse<MedicinePriceResponse>> addPrice(
            @PathVariable int medicineId,
            @RequestBody @Valid MedicinePriceRequest request,
            BindingResult bindingResult) {
        MedicinePriceResponse result = medicinePriceService.addPrice(medicineId, request, bindingResult);
        GlobalResponse<MedicinePriceResponse> response = new GlobalResponse<>(result, Message.success, 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update price (only for prices added today)
     * Endpoint: PUT /warehouse/medicines/{medicineId}/prices
     */
    @PutMapping("/medicines/{medicineId}/prices")
    @Operation(summary = "Update price", description = "Update a price. Can only update prices added today.")
    public ResponseEntity<GlobalResponse<MedicinePriceResponse>> updatePrice(
            @PathVariable int medicineId,
            @RequestBody UpdatePriceRequest request,
            BindingResult bindingResult) {
        MedicinePriceResponse result = medicinePriceService.updatePrice(
                medicineId, request.getOldPrice(), request.getNewPrice(), bindingResult);
        GlobalResponse<MedicinePriceResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete price (only for prices added today)
     * Endpoint: DELETE /warehouse/medicines/{medicineId}/prices
     */
    @DeleteMapping("/medicines/{medicineId}/prices")
    @Operation(summary = "Delete price", description = "Delete a price. Can only delete prices added today.")
    public ResponseEntity<GlobalResponse<Void>> deletePrice(
            @PathVariable int medicineId,
            @RequestBody MedicinePriceRequest request) {
        medicinePriceService.deletePrice(medicineId, request);
        GlobalResponse<Void> response = new GlobalResponse<>(null, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Inner class for update request
    public static class UpdatePriceRequest {
        @Valid
        private MedicinePriceRequest oldPrice;
        @Valid
        private MedicinePriceRequest newPrice;

        public MedicinePriceRequest getOldPrice() {
            return oldPrice;
        }

        public void setOldPrice(MedicinePriceRequest oldPrice) {
            this.oldPrice = oldPrice;
        }

        public MedicinePriceRequest getNewPrice() {
            return newPrice;
        }

        public void setNewPrice(MedicinePriceRequest newPrice) {
            this.newPrice = newPrice;
        }
    }
}
