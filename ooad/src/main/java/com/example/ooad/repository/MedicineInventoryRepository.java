package com.example.ooad.repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.compositekey.MedicineInventoryKey;
import com.example.ooad.domain.entity.MedicineInventory;

@Repository
public interface MedicineInventoryRepository extends JpaRepository<MedicineInventory, MedicineInventoryKey> {
    
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
}
