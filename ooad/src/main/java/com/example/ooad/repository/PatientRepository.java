package com.example.ooad.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    public Page<Patient> findAll(Pageable pageable);
    public Patient findByIdCard(String idCard);
    
    /**
     * Tìm Patient theo Account ID (dùng cho API profile /me)
     */
    @Query("SELECT p FROM Patient p WHERE p.account.accountId = :accountId")
    Optional<Patient> findByAccountId(@Param("accountId") int accountId);
}
