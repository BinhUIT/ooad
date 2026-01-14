package com.example.ooad.controller.doctor;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.response.DoctorDashboardResponse;
import com.example.ooad.dto.response.DoctorRevenueResponse;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.RevenueStatisticResponse.DailyRevenueData;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.service.doctor.interfaces.DoctorService;
import com.example.ooad.service.invoice.interfaces.RevenueService;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name="Bearer Auth")
@Tag(name = "Doctor")
public class DoctorController {
    private final StaffService staffService;
    private final DoctorService doctorService;
    private final RevenueService revenueService;
    
    public DoctorController(StaffService staffService, DoctorService doctorService, RevenueService revenueService) {
        this.doctorService = doctorService;
        this.staffService = staffService;
        this.revenueService = revenueService;
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
    
    @Operation(summary = "Get doctor's monthly revenue statistics")
    @GetMapping("/doctor/revenue/monthly")
    public ResponseEntity<GlobalResponse<DoctorRevenueResponse>> getDoctorMonthlyRevenue(
            Authentication auth,
            @RequestParam(defaultValue = "0") int year) {
        Staff doctor = staffService.getStaffFromAuth(auth);
        if(doctor == null) {
            throw new NotFoundException("Doctor not found");
        }
        
        if(year == 0) {
            year = LocalDate.now().getYear();
        }
        
        DoctorRevenueResponse result = revenueService.getDoctorRevenueStatistics(doctor, year);
        GlobalResponse<DoctorRevenueResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @Operation(summary = "Get doctor's daily revenue for a specific date")
    @GetMapping("/doctor/revenue/daily")
    public ResponseEntity<GlobalResponse<DailyRevenueData>> getDoctorDailyRevenue(
            Authentication auth,
            @RequestParam(required = false) String date) {
        Staff doctor = staffService.getStaffFromAuth(auth);
        if(doctor == null) {
            throw new NotFoundException("Doctor not found");
        }
        
        java.sql.Date targetDate;
        if(date == null || date.isEmpty()) {
            targetDate = java.sql.Date.valueOf(LocalDate.now());
        } else {
            targetDate = java.sql.Date.valueOf(date);
        }
        
        DailyRevenueData result = revenueService.getDailyRevenueByDoctor(targetDate, doctor);
        GlobalResponse<DailyRevenueData> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
