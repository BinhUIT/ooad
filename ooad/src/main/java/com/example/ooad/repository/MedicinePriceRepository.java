package com.example.ooad.repository;

import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.compositekey.MedicinePriceKey;
import com.example.ooad.domain.entity.MedicinePrice;

@Repository
public interface MedicinePriceRepository extends JpaRepository<MedicinePrice, MedicinePriceKey> {
    
    /**
     * Get the current price for a medicine (latest effective date)
     */
    @Query("SELECT mp.unitPrice FROM MedicinePrice mp " +
           "WHERE mp.medicine.medicineId = :medicineId AND mp.medicinePriceId.effectiveDate <= :currentDate " +
           "ORDER BY mp.medicinePriceId.effectiveDate DESC LIMIT 1")
    BigDecimal getCurrentPrice(@Param("medicineId") int medicineId, @Param("currentDate") Date currentDate);
}
