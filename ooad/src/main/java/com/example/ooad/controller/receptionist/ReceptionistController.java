package com.example.ooad.controller.receptionist;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.appointment.interfaces.AppointmentService;
import com.example.ooad.service.reception.interfaces.ReceptionService;
import com.example.ooad.utils.Message;

import jakarta.transaction.Transactional;

@RestController
public class ReceptionistController {
    private final AppointmentService appointmentService;
    private final ReceptionService receptionService;
    public ReceptionistController(AppointmentService appointmentService, ReceptionService receptionsService) {
        this.appointmentService=appointmentService;
        this.receptionService = receptionsService;
    }
    @GetMapping("/receptionist/end_session")
    @Transactional
    public ResponseEntity<GlobalResponse<String>> endSession() {
        appointmentService.endSession();
        receptionService.endSession();
        GlobalResponse<String> result = new GlobalResponse<String>("", Message.success, 200);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
