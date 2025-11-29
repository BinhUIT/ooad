package com.example.ooad.mapper;

import com.example.ooad.domain.entity.RefPaymentMethod;
import com.example.ooad.dto.request.RefPaymentMethodRequest;
import com.example.ooad.dto.response.RefPaymentMethodResponse;

public class RefPaymentMethodMapper {
    
    public static RefPaymentMethod fromRequestToEntity(RefPaymentMethodRequest request) {
        RefPaymentMethod entity = new RefPaymentMethod();
        entity.setMethodCode(request.getMethodCode());
        entity.setMethodName(request.getMethodName());
        entity.setDescription(request.getDescription());
        entity.setActive(request.getIsActive() != null ? request.getIsActive() : false);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        return entity;
    }

    public static void updateEntityFromRequest(RefPaymentMethod entity, RefPaymentMethodRequest request) {
        if (request.getMethodCode() != null) {
            entity.setMethodCode(request.getMethodCode());
        }
        if (request.getMethodName() != null) {
            entity.setMethodName(request.getMethodName());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getIsActive() != null) {
            entity.setActive(request.getIsActive());
        }
        if (request.getSortOrder() != null) {
            entity.setSortOrder(request.getSortOrder());
        }
    }

    public static RefPaymentMethodResponse fromEntityToResponse(RefPaymentMethod entity) {
        return new RefPaymentMethodResponse(
            entity.getPaymentMethodId(),
            entity.getMethodCode(),
            entity.getMethodName(),
            entity.getDescription(),
            entity.isActive(),
            entity.getSortOrder()
        );
    }
}
