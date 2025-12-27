package com.example.ooad.repository;

import java.sql.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
    public List<MedicalRecord> findByReception_ReceptionId(int receptionId);

    public List<MedicalRecord> findByReception_ReceptionIdIn(List<Integer> receptionIds);

    public Page<MedicalRecord> findByReception_ReceptionId(int receptionId, Pageable pageable);

    public Page<MedicalRecord> findByExaminateDate(Date date, Pageable pageable);

    public List<MedicalRecord> findAllByOrderByRecordIdDesc();
}
