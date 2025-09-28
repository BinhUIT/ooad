package com.example.ooad.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EChucVu {
    
    LETAN("Lễ tân"),
    THUKHO("Thủ kho");
    private final String label;
    EChucVu(String label) {
        this.label=label;
    }
    @JsonValue
    public String getLabel() {
        return this.label;
    }
}
