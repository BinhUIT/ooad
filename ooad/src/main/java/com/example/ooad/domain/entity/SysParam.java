package com.example.ooad.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "sys_param")
public class SysParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "param_id")
    private int paramId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private SysParamGroup group;

    @Column(name = "param_code", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    private String paramCode;

    @Column(name = "param_name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String paramName;

    @Column(name = "param_value", columnDefinition = "VARCHAR(255)", nullable = false)
    private String paramValue;

    @Column(name = "data_type", columnDefinition = "VARCHAR(50)")
    private String dataType = "STRING";

    @Column(name = "unit", columnDefinition = "VARCHAR(50)")
    private String unit;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom = LocalDate.now();

    @Column(name = "description", columnDefinition = "VARCHAR(255)")
    private String description;

    @Column(name = "is_active", columnDefinition = "TINYINT(1)")
    private boolean isActive = true;

    public SysParam() {
    }

    public SysParam(int paramId, SysParamGroup group, String paramCode, String paramName, String paramValue,
            String dataType, String unit, LocalDate effectiveFrom, String description, boolean isActive) {
        this.paramId = paramId;
        this.group = group;
        this.paramCode = paramCode;
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.dataType = dataType;
        this.unit = unit;
        this.effectiveFrom = effectiveFrom;
        this.description = description;
        this.isActive = isActive;
    }

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public SysParamGroup getGroup() {
        return group;
    }

    public void setGroup(SysParamGroup group) {
        this.group = group;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

}
