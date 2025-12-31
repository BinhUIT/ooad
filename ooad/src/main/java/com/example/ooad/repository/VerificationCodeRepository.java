package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.VerificationCode;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    public VerificationCode findByCode(String code);
    public List<VerificationCode> findByEmail(String email);
}
