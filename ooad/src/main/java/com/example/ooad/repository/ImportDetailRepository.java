package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.compositekey.ImportDetailKey;
import com.example.ooad.domain.entity.ImportDetail;

@Repository
public interface ImportDetailRepository extends JpaRepository<ImportDetail, ImportDetailKey> {
    
    /**
     * Find all import details by import ID
     */
    @Query("SELECT id FROM ImportDetail id WHERE id.medicineImport.importId = :importId")
    List<ImportDetail> findByImportId(@Param("importId") int importId);
    
    /**
     * Delete all import details by import ID
     */
    @Modifying
    @Query("DELETE FROM ImportDetail id WHERE id.medicineImport.importId = :importId")
    void deleteByImportId(@Param("importId") int importId);
    
    /**
     * Check if medicine is used in any import
     */
    @Query("SELECT COUNT(id) > 0 FROM ImportDetail id WHERE id.medicine.medicineId = :medicineId")
    boolean existsByMedicineId(@Param("medicineId") int medicineId);
}
