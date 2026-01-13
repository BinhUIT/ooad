package com.example.ooad.service.doctor.interfaces;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.response.DoctorDashboardResponse;

public interface DoctorService {
    public DoctorDashboardResponse getDoctorDashboard(Staff staff);
}
