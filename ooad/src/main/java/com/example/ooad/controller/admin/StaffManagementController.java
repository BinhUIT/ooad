package com.example.ooad.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.StaffResponse;
import com.example.ooad.repository.StaffRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/staffs")
@Tag(name = "Staff Management", description = "APIs quản lý nhân viên")
@SecurityRequirement(name = "bearerAuth")
public class StaffManagementController {
    
    private final StaffRepository staffRepository;
    
    public StaffManagementController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }
    
    /**
     * Lấy danh sách tất cả nhân viên
     * GET /admin/staffs
     */
    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    public ResponseEntity<GlobalResponse<List<StaffResponse>>> getAllStaffs() {
        List<Staff> staffs = staffRepository.findAll();
        List<StaffResponse> responses = staffs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new GlobalResponse<>(
                responses,
                "Staff list retrieved successfully", 
                200));
    }
    
    /**
     * Lấy danh sách tất cả doctors
     * GET /admin/staffs/doctors
     */
    @GetMapping("/doctors")
    @Operation(summary = "Lấy danh sách tất cả bác sĩ")
    public ResponseEntity<GlobalResponse<List<StaffResponse>>> getAllDoctors() {
        List<Staff> doctors = staffRepository.findByPositionIgnoreCase("DOCTOR");
        List<StaffResponse> responses = doctors.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new GlobalResponse<>(
                responses,
                "Doctor list retrieved successfully", 
                200));
    }
    
    private StaffResponse mapToResponse(Staff staff) {
        StaffResponse response = new StaffResponse();
        response.setStaffId(staff.getStaffId());
        response.setFullName(staff.getFullName());
        response.setPosition(staff.getPosition());
        response.setDateOfBirth(staff.getDateOfBirth());
        response.setGender(staff.getGender());
        response.setPhone(staff.getPhone());
        response.setEmail(staff.getEmail());
        response.setIdCard(staff.getIdCard());
        return response;
    }
}
