package com.example.ooad.service.refdiseasetype.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.example.ooad.dto.request.RefDiseaseTypeFilterRequest;
import com.example.ooad.dto.request.RefDiseaseTypeRequest;
import com.example.ooad.dto.response.RefDiseaseTypeResponse;

public interface RefDiseaseTypeService {
    RefDiseaseTypeResponse createDiseaseType(RefDiseaseTypeRequest request, BindingResult bindingResult);

    RefDiseaseTypeResponse updateDiseaseType(int id, RefDiseaseTypeRequest request, BindingResult bindingResult);

    RefDiseaseTypeResponse getDiseaseTypeById(int id);

    List<RefDiseaseTypeResponse> getAllDiseaseTypes();

    List<RefDiseaseTypeResponse> getAllActiveDiseaseTypes();

    void deleteDiseaseType(int id);

    Page<RefDiseaseTypeResponse> searchDiseaseTypes(RefDiseaseTypeFilterRequest filter);
}
