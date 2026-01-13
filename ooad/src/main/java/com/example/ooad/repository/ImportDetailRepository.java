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
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM ImportDetail id WHERE id.medicineImport.importId = :importId")
    void deleteByImportId(@Param("importId") int importId);
    
    /**
     * Check if medicine is used in any import
     */
    @Query("SELECT COUNT(id) > 0 FROM ImportDetail id WHERE id.medicine.medicineId = :medicineId")
    boolean existsByMedicineId(@Param("medicineId") int medicineId);
    
    /**
     * Find import detail by import ID and medicine ID (composite key lookup)
     */
    @Query("SELECT id FROM ImportDetail id WHERE id.medicineImport.importId = :importId AND id.medicine.medicineId = :medicineId")
    java.util.Optional<ImportDetail> findByImportIdAndMedicineId(@Param("importId") int importId, @Param("medicineId") int medicineId);

    /**
     * Delete import detail by import ID and medicine ID
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM ImportDetail id WHERE id.medicineImport.importId = :importId AND id.medicine.medicineId = :medicineId")
    void deleteByImportIdAndMedicineId(@Param("importId") int importId, @Param("medicineId") int medicineId);
}
