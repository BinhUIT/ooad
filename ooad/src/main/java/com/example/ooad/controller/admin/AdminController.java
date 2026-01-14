package com.example.ooad.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.AdminStatistic;
import com.example.ooad.dto.response.AppointmentChartData;
import com.example.ooad.dto.response.AppointmentStatistic;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.PatientVisitDetailResponse;
import com.example.ooad.dto.response.PatientVisitReportData;
import com.example.ooad.dto.response.PatientVisitStatistic;
import com.example.ooad.dto.response.StaffScheduleResponse;
import com.example.ooad.service.admin.interfaces.AdminService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "Bearer Auth")
@Tag(name = "Admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(description = "Create Schedule", responses = {
            @ApiResponse(description = Message.staffBusy, responseCode = "400", content = @Content(

                    mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class)

            )),
            @ApiResponse(description = "Success", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/admin/create_schedule")
    public ResponseEntity<GlobalResponse<StaffScheduleResponse>> createSchedule(
            @RequestBody CreateScheduleRequest request) {
        StaffScheduleResponse result = adminService.createSchedule(request);
        GlobalResponse<StaffScheduleResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/appointment_chart")
    public ResponseEntity<GlobalResponse<List<AppointmentChartData>>> getAppointmentChartData(
            @RequestParam(defaultValue = "2025") int year) {
        List<AppointmentChartData> result = adminService.getAppointmentChart(year);
        GlobalResponse<List<AppointmentChartData>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/appointment_statistic")
    public ResponseEntity<GlobalResponse<AppointmentStatistic>> getAppointmentStatistic(
            @RequestParam(defaultValue = "2025") int year) {
        AppointmentStatistic result = adminService.getAppointmentStatistic(year);
        GlobalResponse<AppointmentStatistic> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/statistic")
    public ResponseEntity<GlobalResponse<AdminStatistic>> getAdminStatistic() {
        AdminStatistic result = adminService.getAdminStatistic();
        GlobalResponse<AdminStatistic> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get Patient Visit Report by Year", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class)))
    })
    @GetMapping("/admin/patient_visit_report")
    public ResponseEntity<GlobalResponse<List<PatientVisitReportData>>> getPatientVisitReport(
            @RequestParam(defaultValue = "2025") int year) {
        List<PatientVisitReportData> result = adminService.getPatientVisitReport(year);
        GlobalResponse<List<PatientVisitReportData>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get Patient Visit Statistics by Year", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class)))
    })
    @GetMapping("/admin/patient_visit_statistic")
    public ResponseEntity<GlobalResponse<PatientVisitStatistic>> getPatientVisitStatistic(
            @RequestParam(defaultValue = "2025") int year) {
        PatientVisitStatistic result = adminService.getPatientVisitStatistic(year);
        GlobalResponse<PatientVisitStatistic> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get Patient Visit Details by Year", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class)))
    })
    @GetMapping("/admin/patient_visit_details")
    public ResponseEntity<GlobalResponse<List<PatientVisitDetailResponse>>> getPatientVisitDetails(
            @RequestParam(defaultValue = "2025") int year) {
        List<PatientVisitDetailResponse> result = adminService.getPatientVisitDetails(year);
        GlobalResponse<List<PatientVisitDetailResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
