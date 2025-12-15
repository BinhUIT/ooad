package com.example.ooad.dto.response;

import java.util.List;
import java.util.Map;

/**
 * DTO response cho lịch làm việc của một tháng.
 * Dùng để hiển thị tổng quan calendar view.
 */
public class MonthlyScheduleResponse {
    
    private int month;
    private int year;
    
    // Map: ngày (1-31) -> danh sách summary của các ca trong ngày đó
    private Map<Integer, List<DayScheduleSummary>> scheduleByDay;
    
    // Danh sách bác sĩ và màu tương ứng
    private List<StaffColorMapping> staffColors;
    
    // Tổng số ca trong tháng
    private int totalShifts;
    
    // Tháng này có phải tháng hiện tại không
    private boolean isCurrentMonth;
    
    // Tháng này có phải tháng trong quá khứ không
    private boolean isPastMonth;
    
    // Tháng trước có lịch để copy không
    private boolean hasPreviousMonthSchedule;

    public MonthlyScheduleResponse() {
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Map<Integer, List<DayScheduleSummary>> getScheduleByDay() {
        return scheduleByDay;
    }

    public void setScheduleByDay(Map<Integer, List<DayScheduleSummary>> scheduleByDay) {
        this.scheduleByDay = scheduleByDay;
    }

    public List<StaffColorMapping> getStaffColors() {
        return staffColors;
    }

    public void setStaffColors(List<StaffColorMapping> staffColors) {
        this.staffColors = staffColors;
    }

    public int getTotalShifts() {
        return totalShifts;
    }

    public void setTotalShifts(int totalShifts) {
        this.totalShifts = totalShifts;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public boolean isPastMonth() {
        return isPastMonth;
    }

    public void setPastMonth(boolean pastMonth) {
        isPastMonth = pastMonth;
    }

    public boolean isHasPreviousMonthSchedule() {
        return hasPreviousMonthSchedule;
    }

    public void setHasPreviousMonthSchedule(boolean hasPreviousMonthSchedule) {
        this.hasPreviousMonthSchedule = hasPreviousMonthSchedule;
    }

    /**
     * Summary của một ca trong ngày để hiển thị trên calendar cell.
     */
    public static class DayScheduleSummary {
        private int staffId;
        private String staffName;
        private String staffColor;
        private String shiftType; // MORNING hoặc AFTERNOON
        private String timeRange; // "8:00 - 12:00"
        private int slotCount; // Số slot trong ca

        public DayScheduleSummary() {
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

        public String getStaffColor() {
            return staffColor;
        }

        public void setStaffColor(String staffColor) {
            this.staffColor = staffColor;
        }

        public String getShiftType() {
            return shiftType;
        }

        public void setShiftType(String shiftType) {
            this.shiftType = shiftType;
        }

        public String getTimeRange() {
            return timeRange;
        }

        public void setTimeRange(String timeRange) {
            this.timeRange = timeRange;
        }

        public int getSlotCount() {
            return slotCount;
        }

        public void setSlotCount(int slotCount) {
            this.slotCount = slotCount;
        }
    }

    /**
     * Mapping bác sĩ -> màu để hiển thị legend trên calendar.
     */
    public static class StaffColorMapping {
        private int staffId;
        private String staffName;
        private String color;

        public StaffColorMapping() {
        }

        public StaffColorMapping(int staffId, String staffName, String color) {
            this.staffId = staffId;
            this.staffName = staffName;
            this.color = color;
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

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
