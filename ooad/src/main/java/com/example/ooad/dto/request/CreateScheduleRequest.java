package com.example.ooad.dto.request;

import java.sql.Date;
import java.time.LocalTime;

import com.example.ooad.dto.abstracts.StaffScheduleDto;

public class CreateScheduleRequest extends StaffScheduleDto{
    
    public CreateScheduleRequest(int staffId, Date scheduleDate, LocalTime startTime, LocalTime endTime) {
        super(staffId,scheduleDate,startTime,endTime);
    }
    public CreateScheduleRequest() {
    }
    
}
