package com.example.ooad.domain.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="sys_param")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysParam {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="param_id")
    private int paramId;

    @ManyToOne
    @JoinColumn(name="group_id",nullable=false)
    private SysParamGroup group;

    @Column(name="param_code", columnDefinition="VARCHAR(100)", nullable=false, unique=true) 
    private String paramCode;

    @Column(name="param_name", columnDefinition = "VARCHAR(255)", nullable = false) 
    private String paramName;

    @Column(name="param_value", columnDefinition = "VARCHAR(255)", nullable = false)
    private String paramValue;

    @Column(name="data_type", columnDefinition = "VARCHAR(50)")
    private String dataType="STRING";

    @Column(name="unit", columnDefinition = "VARCHAR(50)")
    private String unit;

    @Column(name="effective_from")
    private Instant effectiveFrom = Instant.now();

     @Column(name="description", columnDefinition="VARCHAR(255)")
    private String description;

    @Column(name="is_active", columnDefinition="TINYINT(1)")
    private boolean isActive=true;

}
