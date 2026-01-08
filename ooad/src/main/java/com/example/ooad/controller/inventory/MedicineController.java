package com.example.ooad.controller.inventory;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.enums.EMedicineUnit;
import com.example.ooad.dto.request.inventory.MedicineFilterRequest;
import com.example.ooad.dto.request.inventory.MedicineRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.inventory.MedicineDetailResponse;
import com.example.ooad.dto.response.inventory.MedicineResponse;
import com.example.ooad.service.inventory.interfaces.MedicineService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/warehouse")
@Tag(name = "Medicine Management", description = "APIs for warehouse staff to manage medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    /**
     * Get paginated list of medicines
     * Endpoint: GET /warehouse/medicines
     */
    @GetMapping("/medicines")
    @Operation(summary = "Get medicine list", description = "Get paginated list of medicines with search and filter options")
    public ResponseEntity<GlobalResponse<Page<MedicineResponse>>> getMedicineList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) EMedicineUnit unit,
            @RequestParam(required = false) String manufacturer) {

        MedicineFilterRequest filter = new MedicineFilterRequest();
        if (page != null)
            filter.setPage(page);
        if (size != null)
            filter.setSize(size);
        if (sortBy != null)
            filter.setSortBy(sortBy);
        if (sortType != null)
            filter.setSortType(sortType);
        if (keyword != null)
            filter.setKeyword(keyword);
        if (unit != null)
            filter.setUnit(unit);
        if (manufacturer != null)
            filter.setManufacturer(manufacturer);

        Page<MedicineResponse> result = medicineService.getMedicineList(filter);
        GlobalResponse<Page<MedicineResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all medicines (non-paginated)
     * Endpoint: GET /warehouse/medicines/all
     */
    @GetMapping("/medicines/all")
    @Operation(summary = "Get all medicines", description = "Get all medicines without pagination")
    public ResponseEntity<GlobalResponse<List<MedicineResponse>>> getAllMedicines() {
        List<MedicineResponse> result = medicineService.getAllMedicines();
        GlobalResponse<List<MedicineResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get medicine detail with inventory and price history
     * Endpoint: GET /warehouse/medicines/{id}
     */
    @GetMapping("/medicines/{id}")
    @Operation(summary = "Get medicine detail", description = "Get medicine detail with inventory batches and price history")
    public ResponseEntity<GlobalResponse<MedicineDetailResponse>> getMedicineDetail(
            @PathVariable int id,
            @RequestParam(defaultValue = "false") boolean includeZeroQuantity) {
        MedicineDetailResponse result = medicineService.getMedicineDetail(id, includeZeroQuantity);
        GlobalResponse<MedicineDetailResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create new medicine
     * Endpoint: POST /warehouse/medicines
     */
    @PostMapping("/medicines")
    @Operation(summary = "Create medicine", description = "Create a new medicine")
    public ResponseEntity<GlobalResponse<MedicineResponse>> createMedicine(
            @RequestBody @Valid MedicineRequest request,
            BindingResult bindingResult) {
        MedicineResponse result = medicineService.createMedicine(request, bindingResult);
        GlobalResponse<MedicineResponse> response = new GlobalResponse<>(result, Message.success, 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update medicine
     * Endpoint: PUT /warehouse/medicines/{id}
     */
    @PutMapping("/medicines/{id}")
    @Operation(summary = "Update medicine", description = "Update medicine information (name, unit, etc.)")
    public ResponseEntity<GlobalResponse<MedicineResponse>> updateMedicine(
            @PathVariable int id,
            @RequestBody @Valid MedicineRequest request,
            BindingResult bindingResult) {
        MedicineResponse result = medicineService.updateMedicine(id, request, bindingResult);
        GlobalResponse<MedicineResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete medicine
     * Endpoint: DELETE /warehouse/medicines/{id}
     */
    @DeleteMapping("/medicines/{id}")
    @Operation(summary = "Delete medicine", description = "Delete medicine (only if not used in any import or prescription)")
    public ResponseEntity<GlobalResponse<Void>> deleteMedicine(@PathVariable int id) {
        medicineService.deleteMedicine(id);
        GlobalResponse<Void> response = new GlobalResponse<>(null, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get distinct manufacturers for dropdown
     * Endpoint: GET /warehouse/medicines/manufacturers
     */
    @GetMapping("/medicines/manufacturers")
    @Operation(summary = "Get manufacturers", description = "Get list of distinct manufacturers for dropdown")
    public ResponseEntity<GlobalResponse<List<String>>> getManufacturers() {
        List<String> result = medicineService.getManufacturers();
        GlobalResponse<List<String>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
