package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Medicine;
@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Integer> {
    public List<Medicine> findByMedicineId_In(List<Integer> medicineIds);
    
} 
