package com.example.ooad.repository;


import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.enums.EReceptionStatus;

public interface ReceptionRepository extends JpaRepository<Reception, Integer> {
    public Page<Reception> findAllByOrderByReceptionDateDesc(Pageable pageable);
    public Page<Reception> findAllByStatusOrderByReceptionDateDesc(EReceptionStatus status, Pageable pageable);
    public Page<Reception> findAllByReceptionDate(Date date, Pageable pageable);
    public Page<Reception> findAllByStatusAndReceptionDate(EReceptionStatus status, Date date,Pageable pageable);
    public List<Reception> findByPatient_PatientId(int patientId);
    public List<Reception> findByReceptionDateLessThanEqual(Date date);
}
