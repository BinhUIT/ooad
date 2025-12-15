package com.example.ooad.dto.request;

import java.sql.Date;

import com.example.ooad.domain.enums.EScheduleStatus;

import jakarta.validation.constraints.NotNull;

/**
 * DTO để phân công ca làm việc (4 time slots liên tiếp).
 * Ca sáng: 8h-12h (4 slots: 8-9, 9-10, 10-11, 11-12)
 * Ca chiều: 13h-17h (4 slots: 13-14, 14-15, 15-16, 16-17)
 */
public class ShiftAssignmentRequest {
    
    @NotNull(message = "Staff ID is required")
    private Integer staffId;
    
    @NotNull(message = "Schedule date is required")
    private Date scheduleDate;
    
    /**
     * Loại ca: MORNING (sáng 8h-12h) hoặc AFTERNOON (chiều 13h-17h)
     */
    @NotNull(message = "Shift type is required")
    private ShiftType shiftType;
    
    private EScheduleStatus status;
    
    public enum ShiftType {
        MORNING,    // 8:00 - 12:00
        AFTERNOON   // 13:00 - 17:00
    }

    public ShiftAssignmentRequest() {
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

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public EScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(EScheduleStatus status) {
        this.status = status;
    }
}
