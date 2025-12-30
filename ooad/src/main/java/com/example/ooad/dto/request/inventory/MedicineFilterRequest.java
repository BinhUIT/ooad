package com.example.ooad.dto.request.inventory;

import com.example.ooad.domain.enums.EMedicineUnit;

public class MedicineFilterRequest {
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "medicineId";
    private String sortType = "ASC"; // ASC or DESC
    private String keyword; // search in medicine name
    private EMedicineUnit unit;
    private String manufacturer;

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

    public EMedicineUnit getUnit() {
        return unit;
    }

    public void setUnit(EMedicineUnit unit) {
        this.unit = unit;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
