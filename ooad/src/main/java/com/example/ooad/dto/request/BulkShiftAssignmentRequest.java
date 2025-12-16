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
    
    @NotEmpty(message = "At least one date is required")
    private List<Date> dates;
    
    @NotNull(message = "Shift type is required")
    private ShiftAssignmentRequest.ShiftType shiftType;
    
    private EScheduleStatus status;
    
    /**
     * Hành động khi gặp lịch trùng:
     * SKIP - Bỏ qua ngày trùng
     * OVERWRITE - Ghi đè lịch cũ
     * CANCEL - Hủy thao tác
     */
    private ShiftAssignmentRequest.ConflictAction conflictAction;

    public BulkShiftAssignmentRequest() {
        this.status = EScheduleStatus.AVAILABLE;
        this.conflictAction = ShiftAssignmentRequest.ConflictAction.SKIP;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }
    
    // Alias for backward compatibility
    public List<Date> getScheduleDates() {
        return dates;
    }

    public void setScheduleDates(List<Date> scheduleDates) {
        this.dates = scheduleDates;
    }
    
    public ShiftAssignmentRequest.ConflictAction getConflictAction() {
        return conflictAction;
    }

    public void setConflictAction(ShiftAssignmentRequest.ConflictAction conflictAction) {
        this.conflictAction = conflictAction;
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
