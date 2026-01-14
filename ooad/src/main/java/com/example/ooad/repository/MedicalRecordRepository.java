package com.example.ooad.repository;

import java.sql.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
    public List<MedicalRecord> findByReception_ReceptionId(int receptionId);

    public List<MedicalRecord> findByReception_ReceptionIdIn(List<Integer> receptionIds);

    public Page<MedicalRecord> findByReception_ReceptionId(int receptionId, Pageable pageable);

    public Page<MedicalRecord> findByExaminateDate(Date date, Pageable pageable);

    public List<MedicalRecord> findAllByOrderByRecordIdDesc();

    @Query("SELECT m FROM MedicalRecord m WHERE m.reception.patient is not null and m.reception.patient.patientId=:patientId")
    public List<MedicalRecord> findByPatient_PatientId(@Param("patientId") int patientId);

    public List<MedicalRecord> findByDoctor_StaffId(int staffId);

    @Query("SELECT m FROM MedicalRecord m WHERE YEAR(m.examinateDate) = :year")
    public List<MedicalRecord> findByYear(@Param("year") int year);

    @Query("SELECT m FROM MedicalRecord m WHERE YEAR(m.examinateDate) = :year AND MONTH(m.examinateDate) = :month")
    public List<MedicalRecord> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(m) FROM MedicalRecord m WHERE m.reception.patient.patientId = :patientId")
    public long countByPatientId(@Param("patientId") int patientId);
}
