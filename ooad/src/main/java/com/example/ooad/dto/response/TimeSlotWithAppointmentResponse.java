package com.example.ooad.dto.response;

import java.time.LocalTime;

import com.example.ooad.domain.enums.EScheduleStatus;

/**
 * DTO response cho một time slot (1 giờ) kèm thông tin appointment và patient nếu có.
 * Dùng để hiển thị chi tiết từng slot trong ca làm việc.
 */
public class TimeSlotWithAppointmentResponse {
    
    private int staffScheduleId;
    private LocalTime startTime;
    private LocalTime endTime;
    private EScheduleStatus status;
    
    // Appointment information (if booked)
    private Integer appointmentId;
    private String appointmentStatus;
    
    // Patient information (if booked)
    private Integer patientId;
    private String patientName;

    public TimeSlotWithAppointmentResponse() {
    }

    public int getStaffScheduleId() {
        return staffScheduleId;
    }

    public void setStaffScheduleId(int staffScheduleId) {
        this.staffScheduleId = staffScheduleId;
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

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
