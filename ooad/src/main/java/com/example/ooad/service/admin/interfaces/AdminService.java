package com.example.ooad.service.admin.interfaces;

import java.util.List;

import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.AdminStatistic;
import com.example.ooad.dto.response.AppointmentChartData;
import com.example.ooad.dto.response.AppointmentStatistic;
import com.example.ooad.dto.response.PatientVisitDetailResponse;
import com.example.ooad.dto.response.PatientVisitReportData;
import com.example.ooad.dto.response.PatientVisitStatistic;
import com.example.ooad.dto.response.StaffScheduleResponse;

public interface AdminService {
    public StaffScheduleResponse createSchedule(CreateScheduleRequest request);

    public List<AppointmentChartData> getAppointmentChart(int year);

    public AppointmentStatistic getAppointmentStatistic(int year);

    public AdminStatistic getAdminStatistic();

    public List<PatientVisitReportData> getPatientVisitReport(int year);

    public PatientVisitStatistic getPatientVisitStatistic(int year);

    public List<PatientVisitDetailResponse> getPatientVisitDetails(int year);
}
