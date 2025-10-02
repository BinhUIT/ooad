package com.example.ooad.dto.request;

import jakarta.validation.constraints.NotNull;

public class LayAcessTokenDto {
    @NotNull
    private String refreshToken;
    @NotNull
    private String tenTaiKhoan;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public LayAcessTokenDto() {
    }

    public LayAcessTokenDto(@NotNull String refreshToken, @NotNull String tenTaiKhoan) {
        this.refreshToken = refreshToken;
        this.tenTaiKhoan = tenTaiKhoan;
    }
    
}
