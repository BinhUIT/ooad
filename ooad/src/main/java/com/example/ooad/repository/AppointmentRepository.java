package com.example.ooad.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.enums.EAppointmentStatus;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{
    public List<Appointment> findByPatient_PatientId(int patientId);
    public List<Appointment> findByStaff_StaffIdAndAppointmentDate(int staffId, Date appointmentDate);
    public Page<Appointment> findByPatient_PatientId(Pageable pageable, int patientId);
    public Page<Appointment> findByPatient_PatientIdAndStatus(Pageable pageable, int patientId, EAppointmentStatus status);
    public Page<Appointment> findByPatient_PatientIdAndAppointmentDate(Pageable pageable, int patientId, Date appointmentDate);
    public Page<Appointment> findByPatient_PatientIdAndAppointmentDateAndStatus(Pageable pageable, int patientId,EAppointmentStatus status, Date appointmentDate);
}
