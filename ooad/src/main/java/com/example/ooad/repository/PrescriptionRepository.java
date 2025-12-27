package com.example.ooad.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    public Page<Prescription> findAll(Pageable pageable);

    public Page<Prescription> findByPrescriptionDate(Pageable pageable, Date prescriptionDate);

    public Optional<Prescription> findByRecord_RecordId(int recordId);

    @Query("SELECT p FROM Prescription p where "
            + "(:prescriptionDate is null or p.prescriptionDate =:prescriptionDate) and "
            + "(:patientName is null or upper(p.record.reception.patient.fullName) like UPPER (CONCAT('%', :patientName, '%')))")
    public Page<Prescription> findPrescriptions(Pageable pageable, @Param("prescriptionDate") Date prescriptionDate,
            @Param("patientName") String patientName);
}
