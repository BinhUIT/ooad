package com.example.ooad.dto.request;

import com.example.ooad.domain.enums.ERole;

public class StaffFilterRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortType;
    private String search; // Search by name, email, phone
    private ERole role; // DOCTOR, RECEPTIONIST, WAREHOUSE_STAFF
    private Boolean active;

    public StaffFilterRequest() {
        this.page = 0;
        this.size = 10;
        this.sortBy = "staffId";
        this.sortType = "ASC";
    }

    // Getters and Setters
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
