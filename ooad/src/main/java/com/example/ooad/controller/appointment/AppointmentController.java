package com.example.ooad.controller.appointment;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EAppointmentStatus;
import com.example.ooad.dto.request.AppointmentRequest;
import com.example.ooad.dto.request.BookAppointmentRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.appointment.interfaces.AppointmentService;
import com.example.ooad.service.patient.interfaces.PatientService;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.Message;



@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final StaffService staffService;
    public AppointmentController(AppointmentService appointmentService, PatientService patientService, StaffService staffService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.staffService = staffService;
    }
    @GetMapping({"/receptionist/appointment_by_id/{appointmentId}","/admin/appointment_by_id/{appointmentId}"})
    public ResponseEntity<GlobalResponse<Appointment>> getAppointmentById(@PathVariable int appointmentId) {
        Appointment result = appointmentService.findAppointmentById(appointmentId);
        GlobalResponse<Appointment> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping({"/receptionist/appointments","/admin/appointments"})
    public ResponseEntity<GlobalResponse<Page<Appointment>>> getAppointmentsList(@RequestParam(defaultValue = "0") int pageNumber,
@RequestParam(defaultValue = "7") int pageSize, @RequestParam("patientName") Optional<String> patientName, @RequestParam("status") Optional<EAppointmentStatus> status, @RequestParam("appointmentDate") Optional<Date> appointmentDate) {
     Page<Appointment> result = appointmentService.getAppointmens(pageNumber, pageSize, patientName, status, appointmentDate);
    GlobalResponse<Page<Appointment>> response = new GlobalResponse<>(result, Message.success, 200);
    return new ResponseEntity<>(response, HttpStatus.OK);
}

    @PostMapping("/patient/book_appointment") 
    public ResponseEntity<GlobalResponse<Appointment>> bookAppointment(@RequestBody BookAppointmentRequest request, Authentication auth) {
        Patient p = patientService.getPatientFromAuth(auth);
        Appointment result = appointmentService.bookAppointment(request, p);
        GlobalResponse<Appointment> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/receptionist/book_appointment") 
    public ResponseEntity<GlobalResponse<Appointment>> receptionistBookAppointment(@RequestBody AppointmentRequest request) {
        Appointment result = appointmentService.receptionistBookAppointment(request);
        GlobalResponse<Appointment> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/unsecure/list_doctor") 
    public ResponseEntity<GlobalResponse<List<Staff>>> getListDoctor() {
        List<Staff> result = staffService.findStaffByRole("Doctor");
        GlobalResponse<List<Staff>> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/unsecure/doctor_schedule")
    public ResponseEntity<GlobalResponse<List<StaffSchedule>>> getScheduleOfDoctor(@RequestParam(required=true) int doctorId, @RequestParam(required=true) Date selectedDate) {
        List<StaffSchedule> result = appointmentService.getScheduleOfDoctor(doctorId, selectedDate);
        GlobalResponse<List<StaffSchedule>> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/patient/appointment_history")
    public ResponseEntity<GlobalResponse<Page<Appointment>>> getAppointmentHistory(@RequestParam(defaultValue = "0") int pageNumber,
    @RequestParam(defaultValue = "7") int pageSize, Optional<EAppointmentStatus> status, Optional<Date> appointmentDate, Authentication auth) {
        Patient p = patientService.getPatientFromAuth(auth);
        Page<Appointment> result = appointmentService.getAppointmentHistory(pageNumber, pageSize, p, status, appointmentDate);
        GlobalResponse<Page<Appointment>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/patient/appointment/{appointmentId}")
    public ResponseEntity<GlobalResponse<Appointment>> patientGetAppointmentById(@PathVariable int appointmentId, Authentication auth) {
        Patient p = patientService.getPatientFromAuth(auth);
        Appointment result = appointmentService.patientGetAppointmentById(p, appointmentId);
        GlobalResponse<Appointment> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping({"/patient/change_appointment_status/{appointmentId}","/receptionist/change_appointment_status/{appointmentId}"})
    public ResponseEntity<GlobalResponse<Appointment>> changeAppointmentStatus(@PathVariable int appointmentId, Authentication auth, @RequestParam(defaultValue="CANCELLED") EAppointmentStatus status) {
        Appointment result = appointmentService.changeAppointmentStatus(auth, appointmentId, status);
        GlobalResponse<Appointment> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping({"/patient/appointment/update/{appointmentId}","/receptionist/appointment/update/{appointmentId}"})
    public ResponseEntity<GlobalResponse<Appointment>> updateAppointment(@PathVariable int appointmentId, @RequestBody AppointmentRequest request, Authentication auth) {
        Appointment result = appointmentService.editAppointment(auth, appointmentId, request);
        GlobalResponse<Appointment> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } 



}
