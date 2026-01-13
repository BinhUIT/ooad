package com.example.ooad.service.admin.interfaces;

import java.util.List;

import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.AdminStatistic;
import com.example.ooad.dto.response.AppointmentChartData;
import com.example.ooad.dto.response.AppointmentStatistic;
import com.example.ooad.dto.response.StaffScheduleResponse;

public interface AdminService {
    public StaffScheduleResponse createSchedule(CreateScheduleRequest request);
    public List<AppointmentChartData> getAppointmentChart(int year);
    public AppointmentStatistic getAppointmentStatistic(int year);
    public AdminStatistic getAdminStatistic();
}
