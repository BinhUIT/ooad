package com.example.ooad.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.MedicineImport;

@Repository
public interface MedicineImportRepository
        extends JpaRepository<MedicineImport, Integer>, JpaSpecificationExecutor<MedicineImport> {

    /**
     * Get distinct supplier names for dropdown
     */
    @Query("SELECT DISTINCT mi.supplier FROM MedicineImport mi WHERE mi.supplier IS NOT NULL ORDER BY mi.supplier")
    List<String> findDistinctSuppliers();

    /**
     * Find imports by date range
     */
    @Query("SELECT mi FROM MedicineImport mi WHERE mi.importDate BETWEEN :fromDate AND :toDate ORDER BY mi.importDate DESC")
    List<MedicineImport> findByDateRange(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    /**
     * Find imports by supplier
     */
    List<MedicineImport> findBySupplierContainingIgnoreCase(String supplier);

    /**
     * Get top 5 recent imports
     */
    List<MedicineImport> findTop5ByOrderByImportDateDesc();

    /**
     * Sum total value of imports between dates
     */
    @Query("SELECT COALESCE(SUM(mi.totalValue), 0) FROM MedicineImport mi WHERE mi.importDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal sumTotalValueByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
