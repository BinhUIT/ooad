package com.example.ooad.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EGioiTinh {
    NAM("Nam"),
    NU("Nữ");
    private final String label;
    EGioiTinh (String label) {
        this.label=label;
    }
    @JsonValue
    public String getLabel() {
        return this.label;
    }
}
