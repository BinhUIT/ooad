package com.example.ooad.domain.entity;

import java.sql.Date;
import java.time.LocalTime;

import com.example.ooad.domain.enums.EScheduleStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="staff_schedule",
uniqueConstraints={
    @UniqueConstraint(columnNames={"staff_id","schedule_date"})
},
indexes = {
    @Index(columnList = "staff_id, schedule_date")
}
)
public class StaffSchedule {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int scheduleId;

    @ManyToOne
    @JoinColumn(name="staff_id")
    private Staff staff;

    private Date scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private EScheduleStatus status=EScheduleStatus.AVAILABLE;

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
