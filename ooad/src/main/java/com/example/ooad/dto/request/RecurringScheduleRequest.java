package com.example.ooad.dto.request;

import com.example.ooad.domain.enums.EScheduleStatus;

import jakarta.validation.constraints.NotNull;

/**
 * DTO để lặp lại lịch làm việc theo ngày trong tuần.
 * Ví dụ: Lặp lại thứ 3 hàng tuần trong tháng 12.
 */
public class RecurringScheduleRequest {
    
    @NotNull(message = "Staff ID is required")
    private Integer staffId;
    
    /**
     * Ngày trong tuần (1 = Thứ 2, ..., 7 = Chủ nhật)
     * Theo ISO-8601
     */
    @NotNull(message = "Day of week is required")
    private Integer dayOfWeek;
    
    @NotNull(message = "Shift type is required")
    private ShiftAssignmentRequest.ShiftType shiftType;
    
    /**
     * Tháng cần lặp (1-12)
     */
    @NotNull(message = "Month is required")
    private Integer month;
    
    /**
     * Năm
     */
    @NotNull(message = "Year is required")
    private Integer year;
    
    private EScheduleStatus status;
    
    /**
     * Hành động khi gặp lịch trùng:
     * SKIP - Bỏ qua ngày trùng
     * OVERWRITE - Ghi đè lịch cũ
     * CANCEL - Hủy thao tác
     */
    private ConflictAction conflictAction;
    
    public enum ConflictAction {
        SKIP,       // Bỏ qua ngày trùng, thêm các ngày không trùng
        OVERWRITE,  // Ghi đè lịch cũ
        CANCEL      // Hủy toàn bộ thao tác
    }

    public RecurringScheduleRequest() {
        this.status = EScheduleStatus.AVAILABLE;
        this.conflictAction = ConflictAction.SKIP;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public ShiftAssignmentRequest.ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftAssignmentRequest.ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public EScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(EScheduleStatus status) {
        this.status = status;
    }

    public ConflictAction getConflictAction() {
        return conflictAction;
    }

    public void setConflictAction(ConflictAction conflictAction) {
        this.conflictAction = conflictAction;
    }
}
