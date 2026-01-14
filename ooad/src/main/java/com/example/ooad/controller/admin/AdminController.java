package com.example.ooad.controller.admin;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.AdminStatistic;
import com.example.ooad.dto.response.AppointmentChartData;
import com.example.ooad.dto.response.AppointmentStatistic;
import com.example.ooad.dto.response.DoctorRevenueResponse;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.RevenueStatisticResponse;
import com.example.ooad.dto.response.RevenueStatisticResponse.DailyRevenueData;
import com.example.ooad.dto.response.StaffScheduleResponse;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.service.admin.interfaces.AdminService;
import com.example.ooad.service.invoice.interfaces.RevenueService;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name="Bearer Auth")
@Tag(name = "Admin")
public class AdminController {
    private final AdminService adminService;
    private final RevenueService revenueService;
    private final StaffService staffService;
    
    public AdminController(AdminService adminService, RevenueService revenueService, StaffService staffService) {
        this.adminService = adminService;
        this.revenueService = revenueService;
        this.staffService = staffService;
    }
    @Operation(
        description="Create Schedule",
        responses={
            @ApiResponse(
                description=Message.staffBusy,
                responseCode="400",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
            @ApiResponse(
                description="Success",
                content = @Content(
                    mediaType="application/json"
                )
            )
        }
    )
    @PostMapping("/admin/create_schedule")
    public ResponseEntity<GlobalResponse<StaffScheduleResponse>> createSchedule(@RequestBody CreateScheduleRequest request) {
        StaffScheduleResponse result = adminService.createSchedule(request);
        GlobalResponse<StaffScheduleResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/appointment_chart") 
    public ResponseEntity<GlobalResponse<List<AppointmentChartData>>> getAppointmentChartData(@RequestParam(defaultValue="2025") int year) {
        List<AppointmentChartData> result = adminService.getAppointmentChart(year);
        GlobalResponse<List<AppointmentChartData>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/appointment_statistic")
    public ResponseEntity<GlobalResponse<AppointmentStatistic>> getAppointmentStatistic(@RequestParam(defaultValue="2025") int year) {
        AppointmentStatistic result = adminService.getAppointmentStatistic(year);
        GlobalResponse<AppointmentStatistic> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/admin/statistic") 
    public ResponseEntity<GlobalResponse<AdminStatistic>> getAdminStatistic() {
        AdminStatistic result = adminService.getAdminStatistic();
        GlobalResponse<AdminStatistic> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @Operation(summary = "Get overall monthly revenue statistics for a year")
    @GetMapping("/admin/revenue/monthly")
    public ResponseEntity<GlobalResponse<RevenueStatisticResponse>> getMonthlyRevenue(
            @RequestParam(defaultValue = "0") int year) {
        if(year == 0) {
            year = LocalDate.now().getYear();
        }
        RevenueStatisticResponse result = revenueService.getMonthlyRevenueByYear(year);
        GlobalResponse<RevenueStatisticResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @Operation(summary = "Get daily revenue for a specific date")
    @GetMapping("/admin/revenue/daily")
    public ResponseEntity<GlobalResponse<DailyRevenueData>> getDailyRevenue(
            @RequestParam(required = false) String date) {
        java.sql.Date targetDate;
        if(date == null || date.isEmpty()) {
            targetDate = java.sql.Date.valueOf(LocalDate.now());
        } else {
            targetDate = java.sql.Date.valueOf(date);
        }
        DailyRevenueData result = revenueService.getDailyRevenue(targetDate);
        GlobalResponse<DailyRevenueData> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @Operation(summary = "Get monthly revenue statistics for a specific doctor")
    @GetMapping("/admin/revenue/doctor/{doctorId}")
    public ResponseEntity<GlobalResponse<DoctorRevenueResponse>> getDoctorRevenue(
            @PathVariable int doctorId,
            @RequestParam(defaultValue = "0") int year) {
        Staff doctor = staffService.findStaffById(doctorId);
        if(doctor == null) {
            throw new NotFoundException("Doctor not found");
        }
        
        if(year == 0) {
            year = LocalDate.now().getYear();
        }
        
        DoctorRevenueResponse result = revenueService.getMonthlyRevenueByYearAndDoctor(year, doctor);
        GlobalResponse<DoctorRevenueResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @Operation(summary = "Get daily revenue for a specific doctor on a specific date")
    @GetMapping("/admin/revenue/doctor/{doctorId}/daily")
    public ResponseEntity<GlobalResponse<DailyRevenueData>> getDoctorDailyRevenue(
            @PathVariable int doctorId,
            @RequestParam(required = false) String date) {
        Staff doctor = staffService.findStaffById(doctorId);
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
