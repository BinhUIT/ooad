package com.example.ooad.domain.entity;

import java.sql.Date;
import java.time.LocalTime;

import com.example.ooad.domain.enums.EScheduleStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Entity lưu lịch làm việc của nhân viên (bác sĩ).
 * Mỗi record là 1 time slot (mặc định 1 giờ).
 * Unique constraint: (staff_id, schedule_date, start_time) - mỗi bác sĩ chỉ có 1 slot tại 1 thời điểm.
 */
@Entity
@Table(name="staff_schedule",
uniqueConstraints={
    @UniqueConstraint(name = "uk_staff_slot", columnNames={"staff_id", "schedule_date", "start_time"})
},
indexes = {
    @Index(name = "idx_staff_schedule_date", columnList = "staff_id, schedule_date")
}
)
public class StaffSchedule {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private int scheduleId;

    @ManyToOne
    @JoinColumn(name="staff_id", nullable = false)
    private Staff staff;

    @Column(name = "schedule_date", nullable = false)
    private Date scheduleDate;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EScheduleStatus status = EScheduleStatus.AVAILABLE;

    public StaffSchedule() {
    }

    public StaffSchedule(LocalTime endTime, Date scheduleDate, int scheduleId, Staff staff, LocalTime startTime) {
        this.endTime = endTime;
        this.scheduleDate = scheduleDate;
        this.scheduleId = scheduleId;
        this.staff = staff;
        this.startTime = startTime;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
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

}
