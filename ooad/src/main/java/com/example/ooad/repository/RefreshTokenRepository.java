package com.example.ooad.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{
    public RefreshToken findByToken(String token);
    public void deleteByToken(String token);
    public List<RefreshToken> findByExpireAtBefore(Date expireAt);
}
