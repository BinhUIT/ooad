package com.example.ooad.service.admin;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.StaffScheduleResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.mapper.StaffScheduleMapper;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.service.staff.StaffService;
import com.example.ooad.utils.DateTimeUtil;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.StaffScheduleValidator;

@Service
public class AdminService {
    private final StaffService staffService;
    private final StaffScheduleRepository staffScheduleRepo;
    private final StaffScheduleValidator staffScheduleValidator;
    public AdminService(StaffService staffService, StaffScheduleRepository staffScheduleRepo, StaffScheduleValidator staffScheduleValidator) {
        this.staffService= staffService;
        this.staffScheduleRepo = staffScheduleRepo;
        this.staffScheduleValidator= staffScheduleValidator;
    }

    public StaffScheduleResponse createSchedule(CreateScheduleRequest request) {
        staffScheduleValidator.validateCreateScheduleRequest(request);
        Staff staff = staffService.findStaffById(request.getStaffId());
        StaffSchedule staffSchedule = StaffScheduleMapper.getScheduleFromRequestAndStaff(request, staff);
        try {
            staffSchedule=staffScheduleRepo.save(staffSchedule);
        }
        catch(DataIntegrityViolationException e) {
            throw new BadRequestException(Message.staffBusy);
        }
        return StaffScheduleMapper.getResponseFromStaffSchedule(staffSchedule);
        
    }
}
