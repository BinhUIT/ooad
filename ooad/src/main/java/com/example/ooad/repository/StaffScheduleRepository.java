package com.example.ooad.repository;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EScheduleStatus;

@Repository
public interface StaffScheduleRepository extends JpaRepository<StaffSchedule, Integer> {
    
    // Existing method
    List<StaffSchedule> findByStaff_StaffIdAndScheduleDateAndStatusNot(int staffId, Date scheduleDate, EScheduleStatus status);
    
    // ==================== BASIC QUERIES ====================
    
    /**
     * Tìm tất cả lịch của một nhân viên trong khoảng thời gian
     */
    List<StaffSchedule> findByStaff_StaffIdAndScheduleDateBetweenOrderByScheduleDateAscStartTimeAsc(
            int staffId, Date startDate, Date endDate);
    
    /**
     * Tìm tất cả lịch trong khoảng thời gian (cho tất cả nhân viên)
     */
    List<StaffSchedule> findByScheduleDateBetweenOrderByScheduleDateAscStartTimeAsc(Date startDate, Date endDate);
    
    /**
     * Tìm tất cả lịch trong một ngày cụ thể
     */
    List<StaffSchedule> findByScheduleDateOrderByStaff_FullNameAscStartTimeAsc(Date scheduleDate);
    
    /**
     * Tìm lịch của một nhân viên trong một ngày cụ thể
     */
    List<StaffSchedule> findByStaff_StaffIdAndScheduleDateOrderByStartTimeAsc(int staffId, Date scheduleDate);
    
    // ==================== CONFLICT CHECK QUERIES ====================
    
    /**
     * Kiểm tra xem slot đã tồn tại chưa (staff + date + startTime)
     */
    boolean existsByStaff_StaffIdAndScheduleDateAndStartTime(int staffId, Date scheduleDate, LocalTime startTime);
    
    /**
     * Tìm slot cụ thể (staff + date + startTime)
     */
    Optional<StaffSchedule> findByStaff_StaffIdAndScheduleDateAndStartTime(int staffId, Date scheduleDate, LocalTime startTime);
    
    /**
     * Tìm các slots trong khoảng thời gian của một ngày (để check conflict cho shift)
     */
    @Query("SELECT s FROM StaffSchedule s WHERE s.staff.staffId = :staffId " +
           "AND s.scheduleDate = :scheduleDate " +
           "AND s.startTime >= :startTime AND s.startTime < :endTime")
    List<StaffSchedule> findSlotsInTimeRange(
            @Param("staffId") int staffId, 
            @Param("scheduleDate") Date scheduleDate,
            @Param("startTime") LocalTime startTime, 
            @Param("endTime") LocalTime endTime);
    
    // ==================== STATISTICS QUERIES ====================
    
    /**
     * Đếm số slots của nhân viên trong tháng
     */
    @Query("SELECT COUNT(s) FROM StaffSchedule s WHERE s.staff.staffId = :staffId " +
           "AND MONTH(s.scheduleDate) = :month AND YEAR(s.scheduleDate) = :year")
    long countByStaffAndMonth(@Param("staffId") int staffId, @Param("month") int month, @Param("year") int year);
    
    /**
     * Tìm tất cả staff có lịch trong tháng (để tạo legend màu)
     */
    @Query("SELECT DISTINCT s.staff.staffId FROM StaffSchedule s " +
           "WHERE MONTH(s.scheduleDate) = :month AND YEAR(s.scheduleDate) = :year")
    List<Integer> findDistinctStaffIdsByMonth(@Param("month") int month, @Param("year") int year);
    
    // ==================== COPY SCHEDULE QUERIES ====================
    
    /**
     * Tìm tất cả lịch theo tháng/năm (để copy sang tháng mới)
     */
    @Query("SELECT s FROM StaffSchedule s WHERE MONTH(s.scheduleDate) = :month AND YEAR(s.scheduleDate) = :year " +
           "ORDER BY s.scheduleDate ASC, s.startTime ASC")
    List<StaffSchedule> findByMonth(@Param("month") int month, @Param("year") int year);
    
    /**
     * Tìm tất cả lịch của một staff theo tháng/năm
     */
    @Query("SELECT s FROM StaffSchedule s WHERE s.staff.staffId = :staffId " +
           "AND MONTH(s.scheduleDate) = :month AND YEAR(s.scheduleDate) = :year " +
           "ORDER BY s.scheduleDate ASC, s.startTime ASC")
    List<StaffSchedule> findByStaffAndMonth(
            @Param("staffId") int staffId, @Param("month") int month, @Param("year") int year);
    
    // ==================== DELETE QUERIES ====================
    
    /**
     * Xóa tất cả lịch của một staff trong tháng (để overwrite khi copy)
     */
    @Query("DELETE FROM StaffSchedule s WHERE s.staff.staffId = :staffId " +
           "AND MONTH(s.scheduleDate) = :month AND YEAR(s.scheduleDate) = :year")
    void deleteByStaffAndMonth(@Param("staffId") int staffId, @Param("month") int month, @Param("year") int year);
    
    /**
     * Xóa tất cả lịch trong tháng
     */
    @Query("DELETE FROM StaffSchedule s WHERE MONTH(s.scheduleDate) = :month AND YEAR(s.scheduleDate) = :year")
    void deleteByMonth(@Param("month") int month, @Param("year") int year);

    StaffSchedule findByStaff_StaffIdAndStartTimeAndScheduleDate(int staffId, LocalTime startTime, Date scheduleDate);

    public List<StaffSchedule> findByScheduleDate(Date scheduleDate);
}
