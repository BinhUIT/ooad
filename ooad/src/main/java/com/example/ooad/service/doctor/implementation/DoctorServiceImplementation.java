package com.example.ooad.service.doctor.implementation;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.dto.response.DoctorDashboardResponse;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.repository.MedicalRecordRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.service.doctor.interfaces.DoctorService;
import com.example.ooad.utils.DateTimeUtil;
@Service
public class DoctorServiceImplementation implements DoctorService {
    private final AppointmentRepository appointmentRepo;
    private final MedicalRecordRepository medicalRecordRepo;
    public DoctorServiceImplementation(AppointmentRepository appointmentRepo, MedicalRecordRepository medicalRecordRepo) {
        this.appointmentRepo = appointmentRepo;
        this.medicalRecordRepo = medicalRecordRepo;
    }

    @Override
    public DoctorDashboardResponse getDoctorDashboard(Staff staff) {
        Date currentDate = Date.valueOf(LocalDate.now());

        LocalTime currentTime = LocalTime.now();
        DoctorDashboardResponse result = new DoctorDashboardResponse();
        List<Appointment> appointments = appointmentRepo.findByStaff_StaffId(staff.getStaffId());
        result.setAppointmentAmount(appointments.size());
        result.setRecordAmount(medicalRecordRepo.findByDoctor_StaffId(staff.getStaffId()).size());
        result.setTodayAppointment(appointmentRepo.findByStaff_StaffIdAndAppointmentDate(staff.getStaffId(), currentDate));
        List<Appointment> sortedListAppointment = appointments.stream().sorted(Comparator
        .comparing(Appointment::getAppointmentDate).reversed()
        .thenComparing(Appointment::getAppointmentTime).reversed()).toList();
        
        for(int i=0;i<sortedListAppointment.size()-1;i++) {
            Date firstAppointmentDate = sortedListAppointment.get(i).getAppointmentDate();
            LocalTime firstAppointmentTime = sortedListAppointment.get(i).getAppointmentTime();
            Date secondAppointmentDate = sortedListAppointment.get(i+1).getAppointmentDate();
            LocalTime secondAppointmentTime = sortedListAppointment.get(i+1).getAppointmentTime();
            if(DateTimeUtil.isFirstTimeAfterSecondTime(currentDate, currentTime, firstAppointmentDate, firstAppointmentTime)&&DateTimeUtil.isFirstTimeAfterSecondTime(secondAppointmentDate, secondAppointmentTime, currentDate, currentTime)) {
                result.setLatestAppointment(sortedListAppointment.get(i));
                result.setUpcomingAppointment(sortedListAppointment.get(+1));
                break;
            }
        }
        return result;
    }
    
}
