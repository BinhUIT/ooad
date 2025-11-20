package com.example.ooad.validator;

import org.springframework.stereotype.Service;

import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.exception.BadRequestException;

import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.DateTimeUtil;
import com.example.ooad.utils.Message;

@Service
public class StaffScheduleValidator {
    
    private final StaffService staffService;
    public StaffScheduleValidator(StaffService staffService) {
        this.staffService = staffService;
    }
    public  void validateCreateScheduleRequest(CreateScheduleRequest request) {
        if(!DateTimeUtil.isStartBeforeEnd(request.getStartTime(), request.getEndTime())) {
            throw new BadRequestException(Message.invalidStartAndEndTime);
        } 
        if(!DateTimeUtil.isDateAfterCurrentDate(request.getScheduleDate())) {
            throw new BadRequestException(Message.invalidScheduleDate);
        }

        if(!staffService.isStaffFree(request.getScheduleDate(), request.getStartTime(), request.getEndTime(), request.getStaffId())){
            throw new BadRequestException(Message.staffBusy);
        }
    }
}
