package com.example.ooad.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RefDiseaseTypeRequest {

    @NotBlank(message = "Disease code is required")
    @Size(max = 50, message = "Disease code must not exceed 50 characters")
    private String diseaseCode;

    @NotBlank(message = "Disease name is required")
    @Size(max = 200, message = "Disease name must not exceed 200 characters")
    private String diseaseName;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private Boolean isChronic;

    private Boolean isContagious;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    public RefDiseaseTypeRequest() {
        this.isChronic = false;
        this.isContagious = false;
        this.isActive = true;
    }

    public RefDiseaseTypeRequest(String diseaseCode, String diseaseName, String description,
            Boolean isChronic, Boolean isContagious, Boolean isActive) {
        this.diseaseCode = diseaseCode;
        this.diseaseName = diseaseName;
        this.description = description;
        this.isChronic = isChronic != null ? isChronic : false;
        this.isContagious = isContagious != null ? isContagious : false;
        this.isActive = isActive != null ? isActive : true;
    }

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
