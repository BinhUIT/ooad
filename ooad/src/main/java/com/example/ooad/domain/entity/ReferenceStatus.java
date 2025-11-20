package com.example.ooad.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceStatus {
    @Column(name="status_code",unique=true, nullable=false, columnDefinition="VARCHAR(50)")
    protected String statusCode;
    @Column(name="status_name", nullable=false, columnDefinition="VARCHAR(100)")
    private String statusName;
    @Column(name="description", columnDefinition="VARCHAR(255)")
    protected String description;

    @Column(name="is_final", columnDefinition="TINYINT(1)")
    protected boolean isFinal=false;

    @Column(name="is_active",columnDefinition="TINYINT(1)")
    protected boolean isActive=false;

    @Column(name="sort_order")
    protected int sortOrder=0;
}
