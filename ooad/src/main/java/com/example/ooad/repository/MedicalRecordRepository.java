package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.MedicalRecord;
@Repository
public interface  MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
    public List<MedicalRecord> findByReception_ReceptionId(int receptionId);
    public List<MedicalRecord> findByReception_ReceptionIdIn(List<Integer> receptionIds);
    public List<MedicalRecord> findAllByOrderByRecordIdDesc();
}
