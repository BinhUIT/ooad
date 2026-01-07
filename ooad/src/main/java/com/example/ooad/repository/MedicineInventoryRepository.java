package com.example.ooad.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.compositekey.MedicineInventoryKey;
import com.example.ooad.domain.entity.MedicineInventory;

@Repository
public interface MedicineInventoryRepository extends JpaRepository<MedicineInventory, MedicineInventoryKey> {

    @Query("SELECT COALESCE(SUM(mi.quantityInStock), 0) FROM MedicineInventory mi " +
            "WHERE mi.medicine.medicineId = :medicineId AND mi.expiryDate > :minExpiryDate AND mi.quantityInStock > 0")
    int getTotalAvailableQuantity(@Param("medicineId") int medicineId, @Param("minExpiryDate") Date minExpiryDate);

    @Query("SELECT MIN(mi.expiryDate) FROM MedicineInventory mi " +
            "WHERE mi.medicine.medicineId = :medicineId AND mi.expiryDate > :minExpiryDate AND mi.quantityInStock > 0")
    Date getNearestExpiryDate(@Param("medicineId") int medicineId, @Param("minExpiryDate") Date minExpiryDate);

    @Query("SELECT mi FROM MedicineInventory mi WHERE mi.medicine.medicineId = :medicineId ORDER BY mi.expiryDate ASC")
    List<MedicineInventory> findByMedicineId(@Param("medicineId") int medicineId);

    @Query("SELECT COALESCE(SUM(mi.quantityInStock), 0) FROM MedicineInventory mi WHERE mi.medicine.medicineId = :medicineId")
    int getTotalQuantityByMedicineId(@Param("medicineId") int medicineId);

    @Query("SELECT COUNT(mi) FROM MedicineInventory mi WHERE mi.expiryDate <= :expiryDate AND mi.quantityInStock > 0")
    long countExpiringBatches(@Param("expiryDate") Date expiryDate);

    @Query("SELECT COUNT(m) FROM Medicine m WHERE (SELECT COALESCE(SUM(mi.quantityInStock), 0) FROM MedicineInventory mi WHERE mi.medicine = m) <= :threshold")
    long countLowStockMedicines(@Param("threshold") int threshold);
}
