package com.example.ooad.service.appointment.interfaces;

import com.example.ooad.domain.entity.Appointment;

public interface AppointmentService {
    public Appointment findAppointmentById(int appointmentId);
}
