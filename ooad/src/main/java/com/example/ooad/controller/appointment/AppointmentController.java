package com.example.ooad.controller.appointment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.appointment.interfaces.AppointmentService;
import com.example.ooad.utils.Message;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @GetMapping({"/receptionist/appointment_by_id/{appointmentId}","/admin/appointment_by_id/{appointmentId}"})
    public ResponseEntity<GlobalResponse<Appointment>> getAppointmentById(@PathVariable int appointmentId) {
        Appointment result = appointmentService.findAppointmentById(appointmentId);
        GlobalResponse<Appointment> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
