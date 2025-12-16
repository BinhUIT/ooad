package com.example.ooad.dto.request;

import java.sql.Date;
import java.time.LocalTime;

import com.example.ooad.domain.enums.EScheduleStatus;

import jakarta.validation.constraints.NotNull;

/**
 * DTO để tạo/cập nhật một time slot lịch làm việc đơn lẻ.
 */
public class StaffScheduleRequest {
    
    @NotNull(message = "Staff ID is required")
    private Integer staffId;
    
    @NotNull(message = "Schedule date is required")
    private Date scheduleDate;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalTime endTime;
    
    private EScheduleStatus status;

    public StaffScheduleRequest() {
        this.status = EScheduleStatus.AVAILABLE;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
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

    public EScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(EScheduleStatus status) {
        this.status = status;
    }
}
