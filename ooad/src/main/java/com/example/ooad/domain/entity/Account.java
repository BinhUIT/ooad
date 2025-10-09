package com.example.ooad.domain.entity;

import com.example.ooad.domain.enums.ERole;
import com.example.ooad.domain.enums.EStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int accountId;
    private String username;
    private String userPassword;
    @Enumerated(EnumType.STRING)
    private ERole role;
    @Enumerated(EnumType.STRING)
    private EStatus status;
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public ERole getRole() {
        return role;
    }
    public void setRole(ERole role) {
        this.role = role;
    }
    public EStatus getStatus() {
        return status;
    }
    public void setStatus(EStatus status) {
        this.status = status;
    }
    public Account() {
    }
    public Account(int accountId, String username, String userPassword, ERole role, EStatus status) {
        this.accountId = accountId;
        this.username = username;
        this.userPassword = userPassword;
        this.role = role;
        this.status = status;
    }
    

}
