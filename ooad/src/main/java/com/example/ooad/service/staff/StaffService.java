package com.example.ooad.service.staff;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EScheduleStatus;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.utils.DateTimeUtil;
import com.example.ooad.utils.Message;

@Service
public class StaffService {
    private final StaffRepository staffRepo;
    private final StaffScheduleRepository staffScheduleRepo;
    public StaffService(StaffRepository staffRepo, StaffScheduleRepository staffScheduleRepo) {
        this.staffRepo = staffRepo;
        this.staffScheduleRepo=staffScheduleRepo;
    } 
    public Staff findStaffById(int staffId) {
        return staffRepo.findById(staffId).orElseThrow(()->new NotFoundException(Message.staffNotFound));
    }
    public boolean isStaffFree(Date scheduleDate, LocalTime startTime, LocalTime endTime, int staffId) {
        List<StaffSchedule> listSchedules = staffScheduleRepo.findByStaff_StaffIdAndScheduleDateAndStatusNot(staffId, scheduleDate, EScheduleStatus.CANCELLED);
        for(StaffSchedule staffSchedule:listSchedules) {
            if(DateTimeUtil.isTwoScheduleOverlap(startTime, endTime, staffSchedule.getStartTime(), staffSchedule.getEndTime())) {
                return false;
            }
        }
        return true;
    }
}
