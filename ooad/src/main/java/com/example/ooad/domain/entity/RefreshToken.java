package com.example.ooad.domain.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String token;
    private Date expireAt;

    public RefreshToken(Date expireAt, int id, String token) {
        this.expireAt = expireAt;
        this.id = id;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public RefreshToken() {
    }
    
}
