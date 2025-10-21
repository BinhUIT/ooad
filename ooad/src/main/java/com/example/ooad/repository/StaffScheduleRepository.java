package com.example.ooad.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EScheduleStatus;

@Repository
public interface StaffScheduleRepository extends JpaRepository<StaffSchedule, Integer> {
    public List<StaffSchedule> findByStaff_StaffIdAndScheduleDateAndStatusNot(int staffId, Date scheduleDate, EScheduleStatus status);
}
