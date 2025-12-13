package com.example.ooad.mapper;

import com.example.ooad.domain.entity.RefDiseaseType;
import com.example.ooad.dto.request.RefDiseaseTypeRequest;
import com.example.ooad.dto.response.RefDiseaseTypeResponse;

public class RefDiseaseTypeMapper {

    public static RefDiseaseType fromRequestToEntity(RefDiseaseTypeRequest request) {
        RefDiseaseType entity = new RefDiseaseType();
        entity.setDiseaseCode(request.getDiseaseCode());
        entity.setDiseaseName(request.getDiseaseName());
        entity.setDescription(request.getDescription());
        entity.setChronic(request.getIsChronic() != null ? request.getIsChronic() : false);
        entity.setContagious(request.getIsContagious() != null ? request.getIsContagious() : false);
        entity.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        return entity;
    }

    public static void updateEntityFromRequest(RefDiseaseType entity, RefDiseaseTypeRequest request) {
        if (request.getDiseaseCode() != null) {
            entity.setDiseaseCode(request.getDiseaseCode());
        }
        if (request.getDiseaseName() != null) {
            entity.setDiseaseName(request.getDiseaseName());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getIsChronic() != null) {
            entity.setChronic(request.getIsChronic());
        }
        if (request.getIsContagious() != null) {
            entity.setContagious(request.getIsContagious());
        }
        if (request.getIsActive() != null) {
            entity.setActive(request.getIsActive());
        }
    }

    public static RefDiseaseTypeResponse fromEntityToResponse(RefDiseaseType entity) {
        return new RefDiseaseTypeResponse(
                entity.getDiseaseTypeId(),
                entity.getDiseaseCode(),
                entity.getDiseaseName(),
                entity.getDescription(),
                entity.isChronic(),
                entity.isContagious(),
                entity.isActive());
    }
}
