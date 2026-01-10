package com.example.ooad.dto.response;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.domain.enums.EStatus;

public class AccountResponse {
    private int accountId;
    private String username;
    private ERole role;
    private EStatus status;
    private String actorName; // Full name of Staff/Patient
    private String message; // Custom message for specific operations

    public AccountResponse(int accountId, ERole role, EStatus status, String username) {
        this.accountId = accountId;
        this.role = role;
        this.status = status;
        this.username = username;
    }

    public AccountResponse(Account account) {
        this.accountId = account.getAccountId();
        this.role = account.getRole();
        this.status = account.getStatus();
        this.username = account.getUsername();
    }

    // Constructor for link account with actor info
    public AccountResponse(Account account, String actorName, String message) {
        this.accountId = account.getAccountId();
        this.username = account.getUsername();
        this.role = account.getRole();
        this.status = account.getStatus();
        this.actorName = actorName;
        this.message = message;
    }

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

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AccountResponse() {
    }

}
