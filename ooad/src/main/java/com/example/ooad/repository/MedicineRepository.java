package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Medicine;
import com.example.ooad.domain.enums.EMedicineUnit;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer>, JpaSpecificationExecutor<Medicine> {
    public List<Medicine> findByMedicineId_In(List<Integer> medicineIds);

    Page<Medicine> findByMedicineNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Medicine> findByUnit(EMedicineUnit unit, Pageable pageable);

    Page<Medicine> findByManufacturerContainingIgnoreCase(String manufacturer, Pageable pageable);

    boolean existsByMedicineNameIgnoreCase(String medicineName);

    @Query("SELECT DISTINCT m.manufacturer FROM Medicine m WHERE m.manufacturer IS NOT NULL ORDER BY m.manufacturer")
    List<String> findDistinctManufacturers();
}
