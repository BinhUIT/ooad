package com.example.ooad.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.compositekey.MedicineInventoryKey;
import com.example.ooad.domain.entity.MedicineInventory;

@Repository
public interface MedicineInventoryRepository extends JpaRepository<MedicineInventory, MedicineInventoryKey>, JpaSpecificationExecutor<MedicineInventory> {
    
    /**
     * Get total available quantity for a medicine with expiry date after a certain date
     */
    @Query("SELECT COALESCE(SUM(mi.quantityInStock), 0) FROM MedicineInventory mi " +
           "WHERE mi.medicine.medicineId = :medicineId AND mi.expiryDate > :minExpiryDate AND mi.quantityInStock > 0")
    int getTotalAvailableQuantity(@Param("medicineId") int medicineId, @Param("minExpiryDate") Date minExpiryDate);
    
    /**
     * Get the nearest expiry date for a medicine
     */
    @Query("SELECT MIN(mi.expiryDate) FROM MedicineInventory mi " +
           "WHERE mi.medicine.medicineId = :medicineId AND mi.expiryDate > :minExpiryDate AND mi.quantityInStock > 0")
    Date getNearestExpiryDate(@Param("medicineId") int medicineId, @Param("minExpiryDate") Date minExpiryDate);
    
    /**
     * Get total quantity for a medicine (including all batches, even expired)
     */
    @Query("SELECT COALESCE(SUM(mi.quantityInStock), 0) FROM MedicineInventory mi " +
           "WHERE mi.medicine.medicineId = :medicineId")
    int getTotalQuantityByMedicineId(@Param("medicineId") int medicineId);
    
    /**
     * Get all inventory batches for a specific medicine
     */
    @Query("SELECT mi FROM MedicineInventory mi " +
           "WHERE mi.medicine.medicineId = :medicineId " +
           "ORDER BY mi.expiryDate ASC")
    List<MedicineInventory> findByMedicineId(@Param("medicineId") int medicineId);
    
    /**
     * Get all inventory batches for a specific import
     */
    @Query("SELECT mi FROM MedicineInventory mi " +
           "WHERE mi.medicineImport.importId = :importId")
    List<MedicineInventory> findByImportId(@Param("importId") int importId);
    
    /**
     * Delete all inventory records by import ID
     */
    @Modifying
    @Query("DELETE FROM MedicineInventory mi WHERE mi.medicineImport.importId = :importId")
    void deleteByImportId(@Param("importId") int importId);
    
    /**
     * Get nearest expiry date for all batches of a medicine
     */
    @Query("SELECT MIN(mi.expiryDate) FROM MedicineInventory mi " +
           "WHERE mi.medicine.medicineId = :medicineId AND mi.quantityInStock > 0")
    Date getNearestExpiryDateByMedicineId(@Param("medicineId") int medicineId);
}
