package com.example.ooad.dto.request;

public class RefDiseaseTypeFilterRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortType;
    private String keyword;
    private Boolean isActive;
    private Boolean isChronic;
    private Boolean isContagious;

    public RefDiseaseTypeFilterRequest() {
        this.page = 0;
        this.size = 10;
        this.sortBy = "diseaseTypeId";
        this.sortType = "ASC";
    }

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsChronic() {
        return isChronic;
    }

    public void setIsChronic(Boolean isChronic) {
        this.isChronic = isChronic;
    }

    public Boolean getIsContagious() {
        return isContagious;
    }

    public void setIsContagious(Boolean isContagious) {
        this.isContagious = isContagious;
    }
}
