package com.example.ooad.dto.response;

import java.sql.Date;
import java.util.List;

/**
 * DTO response cho kết quả thao tác hàng loạt (bulk operation).
 * Dùng cho các chức năng: copy lịch, lặp lịch, xóa hàng loạt.
 */
public class BulkOperationResponse {
    
    private boolean success;
    private String message;
    
    // Số lượng thành công
    private int successCount;
    
    // Số lượng bị skip (do trùng)
    private int skippedCount;
    
    // Số lượng lỗi
    private int errorCount;
    
    // Danh sách các ngày bị xung đột (nếu có)
    private List<ConflictInfo> conflicts;
    
    // Danh sách các ngày được tạo thành công
    private List<Date> createdDates;

    public BulkOperationResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public List<ConflictInfo> getConflicts() {
        return conflicts;
    }

    public void setConflicts(List<ConflictInfo> conflicts) {
        this.conflicts = conflicts;
    }

    public List<Date> getCreatedDates() {
        return createdDates;
    }

    public void setCreatedDates(List<Date> createdDates) {
        this.createdDates = createdDates;
    }

    /**
     * Thông tin về xung đột lịch.
     */
    public static class ConflictInfo {
        private Date date;
        private int staffId;
        private String staffName;
        private String shiftType;
        private String existingShiftInfo;

        public ConflictInfo() {
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public int getStaffId() {
            return staffId;
        }

        public void setStaffId(int staffId) {
            this.staffId = staffId;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        public String getShiftType() {
            return shiftType;
        }

        public void setShiftType(String shiftType) {
            this.shiftType = shiftType;
        }

        public String getExistingShiftInfo() {
            return existingShiftInfo;
        }

        public void setExistingShiftInfo(String existingShiftInfo) {
            this.existingShiftInfo = existingShiftInfo;
        }
    }
}
