package com.example.ooad.dto.request;

import com.example.ooad.domain.enums.ERole;

public class CreateAccountDto {
    private String username;
    private ERole role;
    private String password;

    public CreateAccountDto(String password, ERole role, String username) {
        this.password = password;
        this.role = role;
        this.username = username;
    }

    public CreateAccountDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
