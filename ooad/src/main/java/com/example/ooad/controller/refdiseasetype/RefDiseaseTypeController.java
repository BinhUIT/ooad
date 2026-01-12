package com.example.ooad.controller.refdiseasetype;

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

import com.example.ooad.dto.request.RefDiseaseTypeFilterRequest;
import com.example.ooad.dto.request.RefDiseaseTypeRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.RefDiseaseTypeResponse;
import com.example.ooad.service.refdiseasetype.interfaces.RefDiseaseTypeService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/admin/disease-types", "/doctor/disease-types" })
@Tag(name = "Disease Type Management")
public class RefDiseaseTypeController {

    private final RefDiseaseTypeService refDiseaseTypeService;

    public RefDiseaseTypeController(RefDiseaseTypeService refDiseaseTypeService) {
        this.refDiseaseTypeService = refDiseaseTypeService;
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<RefDiseaseTypeResponse>> createDiseaseType(
            @RequestBody @Valid RefDiseaseTypeRequest request,
            BindingResult bindingResult) {

        RefDiseaseTypeResponse result = refDiseaseTypeService.createDiseaseType(request, bindingResult);
        GlobalResponse<RefDiseaseTypeResponse> response = new GlobalResponse<>(result, Message.success, 201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<RefDiseaseTypeResponse>> updateDiseaseType(
            @PathVariable int id,
            @RequestBody @Valid RefDiseaseTypeRequest request,
            BindingResult bindingResult) {

        RefDiseaseTypeResponse result = refDiseaseTypeService.updateDiseaseType(id, request, bindingResult);
        GlobalResponse<RefDiseaseTypeResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<RefDiseaseTypeResponse>> getDiseaseTypeById(@PathVariable int id) {
        RefDiseaseTypeResponse result = refDiseaseTypeService.getDiseaseTypeById(id);
        GlobalResponse<RefDiseaseTypeResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<Page<RefDiseaseTypeResponse>>> searchDiseaseTypes(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isChronic,
            @RequestParam(required = false) Boolean isContagious) {
        RefDiseaseTypeFilterRequest filter = new RefDiseaseTypeFilterRequest();
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
        if (isActive != null)
            filter.setIsActive(isActive);
        if (isChronic != null)
            filter.setIsChronic(isChronic);
        if (isContagious != null)
            filter.setIsContagious(isContagious);

        Page<RefDiseaseTypeResponse> result = refDiseaseTypeService.searchDiseaseTypes(filter);
        GlobalResponse<Page<RefDiseaseTypeResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalResponse<List<RefDiseaseTypeResponse>>> getAllDiseaseTypes() {
        List<RefDiseaseTypeResponse> result = refDiseaseTypeService.getAllDiseaseTypes();
        GlobalResponse<List<RefDiseaseTypeResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<GlobalResponse<List<RefDiseaseTypeResponse>>> getAllActiveDiseaseTypes() {
        List<RefDiseaseTypeResponse> result = refDiseaseTypeService.getAllActiveDiseaseTypes();
        GlobalResponse<List<RefDiseaseTypeResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteDiseaseType(@PathVariable int id) {
        refDiseaseTypeService.deleteDiseaseType(id);
        GlobalResponse<Void> response = new GlobalResponse<>(null, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
