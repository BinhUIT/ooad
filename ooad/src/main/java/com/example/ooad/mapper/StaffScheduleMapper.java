package com.example.ooad.mapper;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EScheduleStatus;
import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.StaffScheduleResponse;

public class StaffScheduleMapper {
    public static StaffSchedule getScheduleFromRequestAndStaff(CreateScheduleRequest request, Staff staff) {
        StaffSchedule staffSchedule = new StaffSchedule();
        staffSchedule.setEndTime(request.getEndTime());
        staffSchedule.setStartTime(request.getStartTime()); 
        staffSchedule.setScheduleDate(request.getScheduleDate());
        staffSchedule.setStaff(staff);
        staffSchedule.setStatus(EScheduleStatus.AVAILABLE);
        return staffSchedule;
    }
    public static StaffScheduleResponse getResponseFromStaffSchedule(StaffSchedule staffSchedule) {
        return new StaffScheduleResponse(staffSchedule.getStaff().getStaffId(), staffSchedule.getScheduleDate(), staffSchedule.getStartTime(), staffSchedule.getEndTime(), staffSchedule.getScheduleId());
    }
}
