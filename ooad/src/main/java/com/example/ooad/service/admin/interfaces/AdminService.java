package com.example.ooad.service.admin.interfaces;

import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.StaffScheduleResponse;

public interface AdminService {
    public StaffScheduleResponse createSchedule(CreateScheduleRequest request);
}
