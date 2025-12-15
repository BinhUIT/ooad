package com.example.ooad.dto.response;

import java.sql.Date;
import java.util.List;

/**
 * DTO response cho lịch làm việc của một ngày cụ thể.
 * Dùng để hiển thị chi tiết khi click vào một ô trên calendar.
 */
public class DailyScheduleResponse {
    
    private Date date;
    private int dayOfWeek; // 1 = Monday, 7 = Sunday
    private String dayOfWeekName;
    
    // Danh sách các ca làm việc trong ngày (theo dạng ca 4h)
    private List<ShiftResponse> shifts;
    
    // Tổng số ca trong ngày
    private int totalShifts;
    
    // Có phải ngày trong quá khứ không (không cho phép sửa)
    private boolean isPastDate;
    
    // Có phải ngày hôm nay không
    private boolean isToday;

    public DailyScheduleResponse() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }

    public void setDayOfWeekName(String dayOfWeekName) {
        this.dayOfWeekName = dayOfWeekName;
    }

    public List<ShiftResponse> getShifts() {
        return shifts;
    }

    public void setShifts(List<ShiftResponse> shifts) {
        this.shifts = shifts;
    }

    public int getTotalShifts() {
        return totalShifts;
    }

    public void setTotalShifts(int totalShifts) {
        this.totalShifts = totalShifts;
    }

    public boolean isPastDate() {
        return isPastDate;
    }

    public void setPastDate(boolean pastDate) {
        isPastDate = pastDate;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }
}
