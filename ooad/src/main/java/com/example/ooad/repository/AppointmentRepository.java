package com.example.ooad.repository;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT a FROM Appointment a WHERE "+"(a.patient is not null) and "+ "(:status IS NULL or a.status =:status) AND "+ "(:appointmentDate IS NULL or a.appointmentDate =:appointmentDate) AND"+"(:patientName IS NULL or UPPER(a.patient.fullName) LIKE UPPER (CONCAT('%', :patientName, '%')))")
    public Page<Appointment> getAppointments(Pageable pageable, @Param("status") EAppointmentStatus status, @Param("appointmentDate") Date appointmentDate, @Param("patientName") String patientName);

    public List<Appointment> findByStatus(EAppointmentStatus status);
    
    // Find appointment by staff, date and time slot
    Optional<Appointment> findByStaff_StaffIdAndAppointmentDateAndAppointmentTime(int staffId, Date appointmentDate, LocalTime appointmentTime);

     @Query("SELECT a FROM Appointment a WHERE "+ "(a.staff.staffId=:staffId) and "+"(:status IS NULL or a.status =:status) AND "+ "(:appointmentDate IS NULL or a.appointmentDate =:appointmentDate) AND"+"(:patientName IS NULL or UPPER(a.patient.fullName) LIKE UPPER (CONCAT('%', :patientName, '%')))")
     public Page<Appointment> getAppointmentsOfDoctor(Pageable pageable, @Param("staffId") int staffId,@Param("status") EAppointmentStatus status, @Param("appointmentDate") Date appointmentDate, @Param("patientName") String patientName);

     @Query("SELECT a FROM Appointment a WHERE YEAR(a.appointmentDate)=:year and MONTH(a.appointmentDate)=:month")
     public List<Appointment> getAppointmentsByMonthAndYear(@Param("year") int year, @Param("month") int month);
     
     @Query("SELECT a FROM Appointment a WHERE YEAR(a.appointmentDate)=:year") 
     public List<Appointment> getAppointmentsByYear(@Param("year") int year);

     public List<Appointment> findByAppointmentDate(Date appointmentDate);
}
