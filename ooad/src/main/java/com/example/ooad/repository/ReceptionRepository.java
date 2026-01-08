package com.example.ooad.repository;


import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.enums.EReceptionStatus;

public interface ReceptionRepository extends JpaRepository<Reception, Integer> {
    @Query("SELECT r FROM Reception r where (:status is null or :status = r.status) and (:date is null or :date=r.receptionDate) and (:patientName is null or (r.patient is not null and UPPER(r.patient.fullName) like UPPER (CONCAT('%', :patientName, '%'))))")
    public Page<Reception> filterReception(Pageable pageable, @Param("status") EReceptionStatus status, @Param("date") Date receptionDate, @Param("patientName") String patientName);
    public Page<Reception> findAllByOrderByReceptionDateDesc(Pageable pageable);
    public Page<Reception> findAllByStatusOrderByReceptionDateDesc(EReceptionStatus status, Pageable pageable);
    public Page<Reception> findAllByReceptionDate(Date date, Pageable pageable);
    public Page<Reception> findAllByStatusAndReceptionDate(EReceptionStatus status, Date date,Pageable pageable);
    public List<Reception> findByPatient_PatientId(int patientId);
    public List<Reception> findByReceptionDateLessThanEqual(Date date);
    public List<Reception> findByPatient_PatientIdAndStatusAndReceptionDate(int patientId, EReceptionStatus status, Date receptionDate);
}
