package com.example.ooad.dto.request;

import jakarta.validation.constraints.NotNull;

public class LogoutDto {
    @NotNull
    private String acessToken;
    @NotNull
    private String refreshToken;

    public LogoutDto(String acessToken, String refreshToken) {
        this.acessToken = acessToken;
        this.refreshToken = refreshToken;
    }

    public LogoutDto() {
    }

    

    public String getAcessToken() {
        return acessToken;
    }

    public void setAcessToken(String acessToken) {
        this.acessToken = acessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    

}
