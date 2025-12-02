package com.example.ooad.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sys_param_group")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysParamGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int groupId;

    @Column(name = "group_code", unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String groupCode;

    @Column(name = "group_name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String groupName;

    @Column(name = "description", columnDefinition = "VARCHAR(255)")
    private String description;

    @Column(name = "is_active", columnDefinition = "TINYINT(1)")
    private boolean isActive = true;
}
