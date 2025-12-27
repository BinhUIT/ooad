package com.example.ooad.dto.response;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import com.example.ooad.domain.enums.EScheduleStatus;
import com.example.ooad.dto.request.ShiftAssignmentRequest;

/**
 * DTO response cho một ca làm việc (4 time slots liên tiếp).
 * Dùng để hiển thị trên giao diện phân công (view theo ca, không phải slot).
 */
public class ShiftResponse {
    
    private int staffId;
    private String staffName;
    private String staffPosition;
    private String staffColor; // Màu của bác sĩ để hiển thị trên calendar
    private Date scheduleDate;
    private ShiftAssignmentRequest.ShiftType shiftType;
    private LocalTime startTime;
    private LocalTime endTime;
    private EScheduleStatus status;
    
    // Danh sách các time slot trong ca (bao gồm thông tin appointment và patient)
    private List<TimeSlotWithAppointmentResponse> timeSlots;
    
    // Số slot đã có appointment
    private int bookedSlotsCount;
    private int totalSlotsCount;

    public ShiftResponse() {
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

    public String getStaffPosition() {
        return staffPosition;
    }

    public void setStaffPosition(String staffPosition) {
        this.staffPosition = staffPosition;
    }

    public String getStaffColor() {
        return staffColor;
    }

    public void setStaffColor(String staffColor) {
        this.staffColor = staffColor;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public ShiftAssignmentRequest.ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftAssignmentRequest.ShiftType shiftType) {
        this.shiftType = shiftType;
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

    public List<TimeSlotWithAppointmentResponse> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlotWithAppointmentResponse> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public int getBookedSlotsCount() {
        return bookedSlotsCount;
    }

    public void setBookedSlotsCount(int bookedSlotsCount) {
        this.bookedSlotsCount = bookedSlotsCount;
    }

    public int getTotalSlotsCount() {
        return totalSlotsCount;
    }

    public void setTotalSlotsCount(int totalSlotsCount) {
        this.totalSlotsCount = totalSlotsCount;
    }
}
