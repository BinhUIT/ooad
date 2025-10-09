package com.example.ooad.domain.compositekey;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ImportDetailKey implements Serializable {
    private Integer importId;
    private Integer medicineId;
    public ImportDetailKey(Integer importId, Integer medicineId) {
        this.importId = importId;
        this.medicineId = medicineId;
    }
    public ImportDetailKey() {
    }
    public Integer getImportId() {
        return importId;
    }
    public void setImportId(Integer importId) {
        this.importId = importId;
    }
    public Integer getMedicineId() {
        return medicineId;
    }
    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof ImportDetailKey other) {
            return (Objects.equals(other.importId, this.importId))&&(Objects.equals(other.medicineId, this.medicineId));
        }
        return false;
     }

    @Override
    public int hashCode() { 
        return Objects.hash(importId,medicineId);
    }
    
}
