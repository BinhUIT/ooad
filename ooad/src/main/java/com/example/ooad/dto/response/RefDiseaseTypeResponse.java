package com.example.ooad.dto.response;

public class RefDiseaseTypeResponse {
    private int diseaseTypeId;
    private String diseaseCode;
    private String diseaseName;
    private String description;
    private boolean isChronic;
    private boolean isContagious;
    private boolean isActive;

    public RefDiseaseTypeResponse() {
    }

    public RefDiseaseTypeResponse(int diseaseTypeId, String diseaseCode, String diseaseName,
            String description, boolean isChronic, boolean isContagious, boolean isActive) {
        this.diseaseTypeId = diseaseTypeId;
        this.diseaseCode = diseaseCode;
        this.diseaseName = diseaseName;
        this.description = description;
        this.isChronic = isChronic;
        this.isContagious = isContagious;
        this.isActive = isActive;
    }

    public int getDiseaseTypeId() {
        return diseaseTypeId;
    }

    public void setDiseaseTypeId(int diseaseTypeId) {
        this.diseaseTypeId = diseaseTypeId;
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

    public boolean isChronic() {
        return isChronic;
    }

    public void setChronic(boolean isChronic) {
        this.isChronic = isChronic;
    }

    public boolean isContagious() {
        return isContagious;
    }

    public void setContagious(boolean isContagious) {
        this.isContagious = isContagious;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
