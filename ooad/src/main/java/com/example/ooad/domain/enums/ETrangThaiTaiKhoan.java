package com.example.ooad.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ETrangThaiTaiKhoan {
    DANGSUDUNG("Đang sử dụng"), // có thể dùng
    VOHIEUHOA("Vô hiệu hóa");// ko thể dùng
    private final String label;
    ETrangThaiTaiKhoan(String label) {
        this.label=label;
    }
    @JsonValue
    public String getLabel() {
        return this.label;
    }
}
