package com.example.ooad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.RefDiseaseType;

@Repository
public interface RefDiseaseTypeRepository
        extends JpaRepository<RefDiseaseType, Integer>, JpaSpecificationExecutor<RefDiseaseType> {
    Optional<RefDiseaseType> findByDiseaseCode(String diseaseCode);

    boolean existsByDiseaseCode(String diseaseCode);
}
