package com.example.ooad.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.compositekey.MedicinePriceKey;
import com.example.ooad.domain.entity.MedicinePrice;

@Repository
public interface MedicinePriceRepository extends JpaRepository<MedicinePrice, MedicinePriceKey> {

    @Query("SELECT mp.unitPrice FROM MedicinePrice mp " +
            "WHERE mp.medicine.medicineId = :medicineId AND mp.medicinePriceId.effectiveDate <= :currentDate " +
            "ORDER BY mp.medicinePriceId.effectiveDate DESC LIMIT 1")
    BigDecimal getCurrentPrice(@Param("medicineId") int medicineId, @Param("currentDate") Date currentDate);

    @Query("SELECT mp FROM MedicinePrice mp WHERE mp.medicine.medicineId = :medicineId " +
            "ORDER BY mp.medicinePriceId.effectiveDate DESC")
    List<MedicinePrice> findByMedicineIdOrderByEffectiveDateDesc(@Param("medicineId") int medicineId);

    @Query("SELECT MAX(mi.importPrice) FROM MedicineInventory mi WHERE mi.medicine.medicineId = :medicineId")
    BigDecimal getMaxImportPrice(@Param("medicineId") int medicineId);

    @Modifying
    @Query("DELETE FROM MedicinePrice mp WHERE mp.medicine.medicineId = :medicineId " +
            "AND mp.medicinePriceId.effectiveDate = :effectiveDate AND mp.medicinePriceId.effectiveDate = :today")
    void deleteTodayPrice(@Param("medicineId") int medicineId, @Param("effectiveDate") Date effectiveDate,
            @Param("today") Date today);
}
