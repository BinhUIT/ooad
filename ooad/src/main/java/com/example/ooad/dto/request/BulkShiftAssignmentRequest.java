package com.example.ooad.dto.request;

import java.sql.Date;
import java.util.List;

import com.example.ooad.domain.enums.EScheduleStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO để phân công ca làm việc hàng loạt cho nhiều ngày.
 */
public class BulkShiftAssignmentRequest {
    
    @NotNull(message = "Staff ID is required")
    private Integer staffId;
    
    @NotEmpty(message = "At least one schedule date is required")
    private List<Date> scheduleDates;
    
    @NotNull(message = "Shift type is required")
    private ShiftAssignmentRequest.ShiftType shiftType;
    
    private EScheduleStatus status;

    public BulkShiftAssignmentRequest() {
        this.status = EScheduleStatus.AVAILABLE;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public List<Date> getScheduleDates() {
        return scheduleDates;
    }

    public void setScheduleDates(List<Date> scheduleDates) {
        this.scheduleDates = scheduleDates;
    }

    public ShiftAssignmentRequest.ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftAssignmentRequest.ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public EScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(EScheduleStatus status) {
        this.status = status;
    }
}
