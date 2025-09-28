package com.example.ooad.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EVaiTro {
    BACSI("Bác sĩ"),
    LETAN("Lễ tân"),
    THUKHO("Thủ kho"),
    BENHNHAN("Bệnh nhân");
    private final String label;
    EVaiTro(String label) {
        this.label = label;
    } 
    @JsonValue
    public String getLabel() {
        return this.label;
    }
}
