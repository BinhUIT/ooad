package com.example.ooad.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ref_disease_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RefDiseaseType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="disease_type_id")
    private int diseaseTypeId;
    
    @Column(name="disease_code", columnDefinition = "VARCHAR(50)", unique = true, nullable = false)
    private String diseaseCode;

    @Column(name="disease_name", columnDefinition = "VARCHAR(200)", nullable = false)
    private String diseaseName;

    @Column(name="description", columnDefinition = "VARCHAR(255)")
    private String description;

    @Column(name="is_chronic", columnDefinition = "TINYINT(1)")
    private boolean isChronic=false;

    @Column(name="is_contagious", columnDefinition = "TINYINT(1)")
    private boolean isContagious=false;

    @Column(name="is_active", columnDefinition = "TINYINT(1)")
    private boolean isActive = true;
}
