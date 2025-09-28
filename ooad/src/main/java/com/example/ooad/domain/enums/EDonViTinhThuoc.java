package com.example.ooad.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EDonViTinhThuoc {
    VIEN("Viên"),
    VI("Vỉ"),
    HOP("Hộp"),
    CHAI("Chai"),
    TUYP("Tuýp"),
    ONG("Ống"),
    GOI("Gói"),
    ML("ML"),
    GIOT("Giọt");
    private final String label;
    EDonViTinhThuoc(String label) {
        this.label=label;
    }
    @JsonValue
    public String getLabel() {
        return this.label;
    }
}
