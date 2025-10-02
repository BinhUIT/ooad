package com.example.ooad.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.UnusedAccessToken;

@Repository
public interface UnusedAcessTokenRepository extends JpaRepository<UnusedAccessToken, Integer>{
    public UnusedAccessToken findByToken(String token);
    public List<UnusedAccessToken> findByExpireAtBefore(Date expireAt);
}
