package com.example.ooad.dto.response;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private AccountResponse account;

    public LoginResponse(String accessToken, String refreshToken, AccountResponse account) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.account = account;
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

    public AccountResponse getAccount() {
        return account;
    }

    public void setAccount(AccountResponse account) {
        this.account = account;
    }
    
}
