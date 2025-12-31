package com.example.ooad.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.enums.EGender;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    @Query("SELECT p FROM Patient p WHERE" +" (:idCard = p.idCard)"+" or (:email = p.email)")
    public Patient findPatientByIdCardOrEmail(@Param("idCard") String idCard, @Param("email") String email);
    public Page<Patient> findAll(Pageable pageable);
    public Patient findByIdCard(String idCard);
    
    /**
     * Tìm Patient theo Account ID (dùng cho API profile /me)
     */
    @Query("SELECT p FROM Patient p WHERE p.account.accountId = :accountId")
    Optional<Patient> findByAccountId(@Param("accountId") int accountId);

    @Query("SELECT p FROM Patient p WHERE"+" (:keyWord is null or upper(p.fullName) like upper(concat('%',:keyWord,'%'))) and" +" (:gender is null or :gender=p.gender )")
    public Page<Patient> searchPatient(Pageable pageable, @Param("keyWord") String keyWord, @Param("gender") EGender gender);
    public Patient findByEmail(String email);
    
}
