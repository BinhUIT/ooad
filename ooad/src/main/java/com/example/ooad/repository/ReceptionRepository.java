package com.example.ooad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ooad.domain.entity.Reception;

public interface ReceptionRepository extends JpaRepository<Reception, Integer> {
    
}
