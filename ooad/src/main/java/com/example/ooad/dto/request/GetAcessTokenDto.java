package com.example.ooad.dto.request;

import jakarta.validation.constraints.NotNull;

public class GetAcessTokenDto {
    @NotNull
    private String refreshToken;
    @NotNull
    private String username;

    public GetAcessTokenDto() {
    }

    public GetAcessTokenDto(String refreshToken, String username) {
        this.refreshToken = refreshToken;
        this.username = username;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    
}
