package com.example.ooad.service.appointment.implementation;

import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.service.appointment.interfaces.AppointmentService;
import com.example.ooad.utils.Message;
@Service
public class AppointmentServiceImplementation implements AppointmentService {
    private final AppointmentRepository appointmentRepo;
    public AppointmentServiceImplementation(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    public Appointment findAppointmentById(int appointmentId) {
        return appointmentRepo.findById(appointmentId).orElseThrow(()-> new NotFoundException(Message.appointmentNotFound));
    }
    
}
