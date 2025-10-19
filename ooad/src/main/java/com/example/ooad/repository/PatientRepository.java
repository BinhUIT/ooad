package com.example.ooad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
