package com.example.ooad.dto.response;

import java.util.List;

import com.example.ooad.domain.entity.Appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDashboardResponse {
    private Appointment latestAppointment;
    private Appointment upcomingAppointment;
    private int appointmentAmount;
    private int recordAmount;
    private List<Appointment> todayAppointment;
}
