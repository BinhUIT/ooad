package com.example.ooad.dto.abstracts;

import java.sql.Date;
import java.time.LocalTime;

public class StaffScheduleDto {
    protected int staffId;
    protected Date scheduleDate;
    protected LocalTime startTime;
    protected LocalTime endTime;
    public int getStaffId() {
        return staffId;
    }
    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
    public Date getScheduleDate() {
        return scheduleDate;
    }
    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public StaffScheduleDto() {
    }
    public StaffScheduleDto(int staffId, Date scheduleDate, LocalTime startTime, LocalTime endTime) {
        this.staffId = staffId;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
}
