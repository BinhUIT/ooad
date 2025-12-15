package com.example.ooad.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * DTO để copy lịch làm việc từ tháng trước sang tháng hiện tại/tương lai.
 */
public class CopyScheduleFromPreviousMonthRequest {
    
    /**
     * Tháng nguồn (copy từ đây)
     */
    @NotNull(message = "Source month is required")
    private Integer sourceMonth;
    
    @NotNull(message = "Source year is required")
    private Integer sourceYear;
    
    /**
     * Tháng đích (copy đến đây)
     */
    @NotNull(message = "Target month is required")
    private Integer targetMonth;
    
    @NotNull(message = "Target year is required")
    private Integer targetYear;
    
    /**
     * ID của bác sĩ cần copy lịch (null = copy tất cả bác sĩ)
     */
    private Integer staffId;
    
    /**
     * Ghi đè lịch đã có của tháng đích hay không
     */
    private Boolean overwriteExisting;
    
    /**
     * Hành động khi gặp lịch trùng
     */
    private RecurringScheduleRequest.ConflictAction conflictAction;

    public CopyScheduleFromPreviousMonthRequest() {
        this.overwriteExisting = false;
        this.conflictAction = RecurringScheduleRequest.ConflictAction.SKIP;
    }

    public Integer getSourceMonth() {
        return sourceMonth;
    }

    public void setSourceMonth(Integer sourceMonth) {
        this.sourceMonth = sourceMonth;
    }

    public Integer getSourceYear() {
        return sourceYear;
    }

    public void setSourceYear(Integer sourceYear) {
        this.sourceYear = sourceYear;
    }

    public Integer getTargetMonth() {
        return targetMonth;
    }

    public void setTargetMonth(Integer targetMonth) {
        this.targetMonth = targetMonth;
    }

    public Integer getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(Integer targetYear) {
        this.targetYear = targetYear;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Boolean getOverwriteExisting() {
        return overwriteExisting;
    }

    public void setOverwriteExisting(Boolean overwriteExisting) {
        this.overwriteExisting = overwriteExisting;
    }

    public RecurringScheduleRequest.ConflictAction getConflictAction() {
        return conflictAction;
    }

    public void setConflictAction(RecurringScheduleRequest.ConflictAction conflictAction) {
        this.conflictAction = conflictAction;
    }
}
