package com.example.ooad.controller.admin;

import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.GlobalResponse;
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
@SecurityRequirement(name="Bearer Auth")
@Tag(name = "Admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
            @ApiResponse
        }
    )
    @PostMapping("/admin/create_schedule")
    public ResponseEntity<GlobalResponse<StaffScheduleResponse>> createSchedule(@RequestBody CreateScheduleRequest request) {
        StaffScheduleResponse result = adminService.createSchedule(request);
        GlobalResponse<StaffScheduleResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
