package com.example.ooad.dto.request;

import java.sql.Date;
import java.util.List;

import com.example.ooad.domain.enums.EScheduleStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO để lặp lại lịch làm việc theo ngày trong tuần.
 * Ví dụ: Lặp lại thứ 2 và thứ 4 hàng tuần từ ngày startDate đến endDate.
 */
public class RecurringScheduleRequest {
    
    @NotNull(message = "Staff ID is required")
    private Integer staffId;
    
    /**
     * Ngày bắt đầu
     */
    @NotNull(message = "Start date is required")
    private Date startDate;
    
    /**
     * Ngày kết thúc
     */
    @NotNull(message = "End date is required")
    private Date endDate;
    
    /**
     * Danh sách các ngày trong tuần (1 = Thứ 2, ..., 7 = Chủ nhật)
     * Theo ISO-8601 convention
     */
    @NotEmpty(message = "At least one day of week is required")
    private List<Integer> daysOfWeek;
    
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

    public RecurringScheduleRequest() {
        this.status = EScheduleStatus.AVAILABLE;
        this.conflictAction = ShiftAssignmentRequest.ConflictAction.SKIP;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Integer> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<Integer> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
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

    public ShiftAssignmentRequest.ConflictAction getConflictAction() {
        return conflictAction;
    }

    public void setConflictAction(ShiftAssignmentRequest.ConflictAction conflictAction) {
        this.conflictAction = conflictAction;
    }
}
