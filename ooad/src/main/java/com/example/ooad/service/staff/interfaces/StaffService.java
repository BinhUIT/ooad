package com.example.ooad.service.staff.interfaces;

import java.sql.Date;
import java.time.LocalTime;

import com.example.ooad.domain.entity.Staff;

public interface StaffService {
    public Staff findStaffById(int staffId);
    public boolean isStaffFree(Date scheduleDate, LocalTime startTime, LocalTime endTime, int staffId);
}
