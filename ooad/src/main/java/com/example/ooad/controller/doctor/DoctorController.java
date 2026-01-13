package com.example.ooad.controller.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.response.DoctorDashboardResponse;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.service.doctor.interfaces.DoctorService;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.Message;

@RestController
public class DoctorController {
    private final StaffService staffService;
    private final DoctorService doctorService;
    public DoctorController(StaffService staffService, DoctorService doctorService) {
        this.doctorService = doctorService;
        this.staffService = staffService;
    }

    @GetMapping("/doctor/dashboard")
    public ResponseEntity<GlobalResponse<DoctorDashboardResponse>> getDoctorDashboard(Authentication auth) {
        Staff doctor = staffService.getStaffFromAuth(auth);
        if(doctor==null) {
            throw new NotFoundException("Doctor not found");
        }
        DoctorDashboardResponse result = doctorService.getDoctorDashboard(doctor);
        GlobalResponse<DoctorDashboardResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
