package com.example.ooad.controller.admin;

import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.StaffScheduleResponse;
import com.example.ooad.service.admin.AdminService;
import com.example.ooad.utils.Message;

@RestController
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/admin/create_schedule")
    public ResponseEntity<GlobalResponse<StaffScheduleResponse>> createSchedule(@RequestBody CreateScheduleRequest request) {
        StaffScheduleResponse result = adminService.createSchedule(request);
        GlobalResponse<StaffScheduleResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
