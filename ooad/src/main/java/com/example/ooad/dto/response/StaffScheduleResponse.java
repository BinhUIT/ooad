package com.example.ooad.dto.response;

import java.sql.Date;
import java.time.LocalTime;

import com.example.ooad.dto.abstracts.StaffScheduleDto;

public class StaffScheduleResponse extends StaffScheduleDto {
    private int staffScheduleId;

    public int getStaffScheduleId() {
        return staffScheduleId;
    }

    public void setStaffScheduleId(int staffScheduleId) {
        this.staffScheduleId=staffScheduleId;
    }

    public StaffScheduleResponse() {
    }

    public StaffScheduleResponse(int staffId, Date scheduleDate, LocalTime startTime, LocalTime endTime, int staffScheduleId) {
        super(staffId, scheduleDate, startTime, endTime);
        this.staffScheduleId=staffScheduleId;
    }
    
}
