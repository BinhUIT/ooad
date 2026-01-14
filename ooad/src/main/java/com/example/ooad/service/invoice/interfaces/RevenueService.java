package com.example.ooad.service.invoice.interfaces;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.response.DoctorRevenueResponse;
import com.example.ooad.dto.response.RevenueStatisticResponse;

public interface RevenueService {
    // Get daily revenue by date
    RevenueStatisticResponse.DailyRevenueData getDailyRevenue(java.sql.Date date);
    
    // Get daily revenue by date for specific doctor
    RevenueStatisticResponse.DailyRevenueData getDailyRevenueByDoctor(java.sql.Date date, Staff doctor);
    
    // Get monthly revenue statistics for a year
    RevenueStatisticResponse getMonthlyRevenueByYear(int year);
    
    // Get monthly revenue statistics for a year by specific doctor
    DoctorRevenueResponse getMonthlyRevenueByYearAndDoctor(int year, Staff doctor);
    
    // Get revenue statistics by doctor for current year (for doctor dashboard)
    DoctorRevenueResponse getDoctorRevenueStatistics(Staff doctor, int year);
}
