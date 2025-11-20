package com.example.ooad.domain.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.ooad.domain.enums.ERole;
import com.example.ooad.domain.enums.EStatus;
import com.example.ooad.dto.request.CreateAccountDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="account")
public class Account implements UserDetails {
    private static final String AUTHORITIES_DELIMETER ="::";
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int accountId;
    
    private String username;
    private String userPassword;
    @Enumerated(EnumType.STRING)
    private ERole role=ERole.PATIENT;
    @Enumerated(EnumType.STRING)
    private EStatus status;
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
     @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authorities = this.role.name();
        return Arrays.stream(authorities.split(AUTHORITIES_DELIMETER)).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return this.userPassword;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    public Account(CreateAccountDto dto, String hashedPassword) {
        this.username= dto.getUsername();
        this.role= dto.getRole();
        this.userPassword=hashedPassword;
        this.status=EStatus.ACTIVE;
    }

}
