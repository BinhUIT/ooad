package com.example.ooad.controller.staff;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.StaffScheduleResponse;
import com.example.ooad.mapper.StaffScheduleMapper;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/staff-schedule")
@Tag(name = "Staff Schedule")
public class ViewStaffScheduleController {

    private final StaffScheduleRepository staffScheduleRepo;
    private final StaffService staffService;

    public ViewStaffScheduleController(StaffScheduleRepository staffScheduleRepo, StaffService staffService) {
        this.staffScheduleRepo = staffScheduleRepo;
        this.staffService = staffService;
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<GlobalResponse<List<StaffScheduleResponse>>> getSchedulesByStaffId(
            @PathVariable int staffId) {

        Staff staff = staffService.findStaffById(staffId);

        List<StaffSchedule> schedules = staffScheduleRepo.findAll().stream()
                .filter(s -> s.getStaff().getStaffId() == staffId)
                .collect(Collectors.toList());

        List<StaffScheduleResponse> result = schedules.stream()
                .map(StaffScheduleMapper::getResponseFromStaffSchedule)
                .collect(Collectors.toList());

        GlobalResponse<List<StaffScheduleResponse>> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
