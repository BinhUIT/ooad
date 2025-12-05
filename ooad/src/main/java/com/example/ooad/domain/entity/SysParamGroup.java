package com.example.ooad.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_param_group")
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

    public SysParamGroup() {
    }

    public SysParamGroup(int groupId, String groupCode, String groupName, String description, boolean isActive) {
        this.groupId = groupId;
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.description = description;
        this.isActive = isActive;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
