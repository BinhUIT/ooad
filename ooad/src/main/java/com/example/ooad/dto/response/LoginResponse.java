package com.example.ooad.dto.response;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private TaiKhoanResponse taiKhoan;

    public LoginResponse(String accessToken, String refreshToken, TaiKhoanResponse taiKhoan) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.taiKhoan = taiKhoan;
    }

    public LoginResponse() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TaiKhoanResponse getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoanResponse taiKhoan) {
        this.taiKhoan = taiKhoan;
    }
    
}
