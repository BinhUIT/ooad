package com.example.ooad.service.staff.interfaces;

import java.sql.Date;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.request.StaffFilterRequest;
import com.example.ooad.dto.request.StaffRequest;
import com.example.ooad.dto.response.StaffResponse;

public interface StaffService {
    public Staff findStaffById(int staffId);

    public boolean isStaffFree(Date scheduleDate, LocalTime startTime, LocalTime endTime, int staffId);

    public StaffResponse createStaff(StaffRequest request, BindingResult bindingResult);

    public StaffResponse updateStaff(int id, StaffRequest request, BindingResult bindingResult);

    public StaffResponse getStaffById(int id);

    public Page<StaffResponse> searchStaffs(StaffFilterRequest filter);

    public void deleteStaff(int id);
}
