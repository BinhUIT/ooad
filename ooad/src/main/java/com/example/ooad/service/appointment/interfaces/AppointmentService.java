package com.example.ooad.service.appointment.interfaces;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EAppointmentStatus;
import com.example.ooad.dto.request.BookAppointmentRequest;
import com.example.ooad.dto.request.ReceptionBookAppointmentRequest;
public interface AppointmentService {
    public Appointment findAppointmentById(int appointmentId);
    public Appointment bookAppointment(BookAppointmentRequest request, Patient patient);
    public Page<Appointment> getAppointmentHistory(int pageNumber, int pageSize,Patient patient, Optional<EAppointmentStatus> status, Optional<Date> appointmentDate);
    public Appointment patientGetAppointmentById(Patient patient, int appointmentId);
    public List<StaffSchedule> getScheduleOfDoctor(int doctorId, Date selectedDate);
    public Page<Appointment> getAppointmens(int pageNumber, int pageSize, Optional<String> patientName, Optional<EAppointmentStatus> status, Optional<Date> appointmentDate);
    public void endSession();
    public Appointment receptionistBookAppointment(ReceptionBookAppointmentRequest request);
    public Appointment changeAppointmentStatus(Authentication auth, int appointmentId,EAppointmentStatus status);
    
}
