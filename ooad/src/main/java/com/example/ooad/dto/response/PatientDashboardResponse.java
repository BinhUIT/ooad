package com.example.ooad.dto.response;

import java.util.List;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Prescription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDashboardResponse {
    private Appointment nextAppointment;
    private List<Appointment> recentAppointments;
    private int medicalRecordsAmount;
    private int pendingInvoicesAmount;
    

}
