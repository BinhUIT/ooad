package com.example.ooad.dto.response;

import java.sql.Date;
import java.time.LocalTime;

import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EScheduleStatus;

/**
 * DTO response đầy đủ cho một time slot lịch làm việc.
 * Bao gồm thông tin bác sĩ và appointment.
 */
public class ScheduleSlotResponse {
    
    private int scheduleId;
    private int staffId;
    private String staffName;
    private String staffPosition;
    private Date scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private EScheduleStatus status;
    
    // Thông tin appointment nếu slot đã được đặt
    private boolean hasAppointment;
    private Integer appointmentId;
    private String patientName;
    private Integer patientId;

    public ScheduleSlotResponse() {
    }

    public ScheduleSlotResponse(StaffSchedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.staffId = schedule.getStaff().getStaffId();
        this.staffName = schedule.getStaff().getFullName();
        this.staffPosition = schedule.getStaff().getPosition();
        this.scheduleDate = schedule.getScheduleDate();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.status = schedule.getStatus();
        this.hasAppointment = false;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
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

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
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

    public boolean isHasAppointment() {
        return hasAppointment;
    }

    public void setHasAppointment(boolean hasAppointment) {
        this.hasAppointment = hasAppointment;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}
