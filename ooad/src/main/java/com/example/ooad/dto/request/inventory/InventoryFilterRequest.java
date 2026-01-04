package com.example.ooad.dto.request.inventory;

public class InventoryFilterRequest {
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "medicineId";
    private String sortType = "ASC"; // ASC or DESC
    private String keyword; // search in medicine name
    private Boolean includeOutOfStock = true; // include medicines with 0 quantity

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

    public Boolean getIncludeOutOfStock() {
        return includeOutOfStock;
    }

    public void setIncludeOutOfStock(Boolean includeOutOfStock) {
        this.includeOutOfStock = includeOutOfStock;
    }
}
