package com.example.ooad.service.admin.implementation;


import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EAppointmentStatus;
import com.example.ooad.dto.request.CreateScheduleRequest;
import com.example.ooad.dto.response.AppointmentChartData;
import com.example.ooad.dto.response.AppointmentStatistic;
import com.example.ooad.dto.response.StaffScheduleResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.mapper.StaffScheduleMapper;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.service.admin.interfaces.AdminService;
import com.example.ooad.service.staff.implementation.StaffServiceImplementation;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.StaffScheduleValidator;

@Service
public class AdminServiceImplementation implements AdminService {
    private final StaffService staffService;
    private final StaffScheduleRepository staffScheduleRepo;
    private final StaffScheduleValidator staffScheduleValidator;
    private final AppointmentRepository appointmentRepo;
    public AdminServiceImplementation(StaffServiceImplementation staffService, StaffScheduleRepository staffScheduleRepo,
         StaffScheduleValidator staffScheduleValidator, AppointmentRepository appointmentRepo) {
        this.staffService= staffService;
        this.staffScheduleRepo = staffScheduleRepo;
        this.staffScheduleValidator= staffScheduleValidator;
        this.appointmentRepo = appointmentRepo;
    }
    @Override
    public StaffScheduleResponse createSchedule(CreateScheduleRequest request) {
        staffScheduleValidator.validateCreateScheduleRequest(request);
        Staff staff = staffService.findStaffById(request.getStaffId());
        StaffSchedule staffSchedule = StaffScheduleMapper.getScheduleFromRequestAndStaff(request, staff);
        try {
            staffSchedule=staffScheduleRepo.save(staffSchedule);
        }
        catch(DataIntegrityViolationException e) {
            throw new BadRequestException(Message.staffBusy);
        }
        return StaffScheduleMapper.getResponseFromStaffSchedule(staffSchedule);
        
    }

    @Override
    public List<AppointmentChartData> getAppointmentChart(int year) {
        String[] monthName= {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        List<AppointmentChartData> result = new ArrayList<>();
        for(int i=0;i<12;i++) {
            AppointmentChartData chartData = new AppointmentChartData(monthName[i],0,0,0,0);
            List<Appointment> appointments = appointmentRepo.getAppointmentsByMonthAndYear(year, i+1);
            for(Appointment a: appointments) {
                if(a.getStatus()==EAppointmentStatus.SCHEDULED) {
                    chartData.setWaiting(chartData.getWaiting()+1);
                    continue;
                }
                if(a.getStatus()==EAppointmentStatus.CANCELLED) {
                    chartData.setCancelled(chartData.getCancelled()+1);
                    continue;
                }
                if(a.getStatus()==EAppointmentStatus.NOSHOW) {
                    chartData.setNotshown(chartData.getNotshown()+1);
                    continue;
                }
                if(a.getStatus()==EAppointmentStatus.COMPLETED) {
                    chartData.setCompleted(chartData.getCompleted()+1);
                }
            }
            result.add(chartData);
        }
        return result;

    }
    @Override
    public AppointmentStatistic getAppointmentStatistic(int year) {
        AppointmentStatistic result = new AppointmentStatistic(0,0,0,0,0);
        List<Appointment> appointments = appointmentRepo.getAppointmentsByYear(year);
        for(Appointment a: appointments) {
            if(a.getStatus()==EAppointmentStatus.SCHEDULED) {
                    result.setScheduled(result.getScheduled()+1);
                    continue;
                }
                if(a.getStatus()==EAppointmentStatus.CANCELLED) {
                    result.setCancelled(result.getCancelled()+1);
                    continue;
                }
                if(a.getStatus()==EAppointmentStatus.NOSHOW) {
                    result.setNoshow(result.getNoshow()+1);
                    continue;
                }
                if(a.getStatus()==EAppointmentStatus.COMPLETED) {
                    result.setCompleted(result.getCompleted()+1);
                }
        }
        result.setTotal(appointments.size());
        return result;
    }
}
