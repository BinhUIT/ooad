package com.example.ooad.service.staff;

import java.sql.Date;
import java.util.List;

import com.example.ooad.dto.request.BulkShiftAssignmentRequest;
import com.example.ooad.dto.request.CopyScheduleFromPreviousMonthRequest;
import com.example.ooad.dto.request.RecurringScheduleRequest;
import com.example.ooad.dto.request.ShiftAssignmentRequest;
import com.example.ooad.dto.request.StaffScheduleRequest;
import com.example.ooad.dto.response.BulkOperationResponse;
import com.example.ooad.dto.response.DailyScheduleResponse;
import com.example.ooad.dto.response.MonthlyScheduleResponse;
import com.example.ooad.dto.response.ScheduleSlotResponse;

/**
 * Service interface cho quản lý lịch làm việc của nhân viên.
 * Hỗ trợ: CRUD, phân ca theo shift, copy tháng, lịch định kỳ.
 */
public interface StaffScheduleService {

    // ==================== BASIC CRUD ====================
    
    /**
     * Tạo một time slot đơn lẻ
     * @param request thông tin slot
     * @return slot vừa tạo
     */
    ScheduleSlotResponse createSlot(StaffScheduleRequest request);
    
    /**
     * Lấy thông tin chi tiết một slot
     * @param scheduleId ID của slot
     * @return thông tin slot với appointment nếu có
     */
    ScheduleSlotResponse getSlotById(int scheduleId);
    
    /**
     * Cập nhật trạng thái slot
     * @param scheduleId ID của slot
     * @param request thông tin cập nhật
     * @return slot sau khi cập nhật
     */
    ScheduleSlotResponse updateSlot(int scheduleId, StaffScheduleRequest request);
    
    /**
     * Xóa một slot (chỉ cho phép nếu không phải ngày quá khứ)
     * @param scheduleId ID của slot
     */
    void deleteSlot(int scheduleId);

    // ==================== SHIFT ASSIGNMENT ====================
    
    /**
     * Phân công một ca làm việc (4 time slots)
     * @param request thông tin ca làm việc
     * @return danh sách slots đã tạo
     */
    List<ScheduleSlotResponse> assignShift(ShiftAssignmentRequest request);
    
    /**
     * Phân công nhiều ca làm việc cùng lúc (bulk)
     * @param request danh sách các ca cần phân công
     * @return kết quả thao tác với thông tin conflicts
     */
    BulkOperationResponse bulkAssignShifts(BulkShiftAssignmentRequest request);

    // ==================== RECURRING SCHEDULE ====================
    
    /**
     * Tạo lịch định kỳ hàng tuần
     * @param request thông tin lịch định kỳ
     * @return kết quả thao tác với thông tin conflicts
     */
    BulkOperationResponse createRecurringSchedule(RecurringScheduleRequest request);

    // ==================== COPY FROM PREVIOUS MONTH ====================
    
    /**
     * Copy lịch từ tháng trước sang tháng hiện tại
     * @param request thông tin copy
     * @return kết quả thao tác với thông tin conflicts
     */
    BulkOperationResponse copyFromPreviousMonth(CopyScheduleFromPreviousMonthRequest request);

    // ==================== VIEW SCHEDULES ====================
    
    /**
     * Lấy lịch theo tháng (calendar view cho tất cả staff)
     * @param month tháng (1-12)
     * @param year năm
     * @return lịch theo tháng với legend màu
     */
    MonthlyScheduleResponse getMonthlySchedule(int month, int year);
    
    /**
     * Lấy lịch theo tháng của một staff cụ thể
     * @param staffId ID của staff
     * @param month tháng (1-12)
     * @param year năm
     * @return lịch theo tháng
     */
    MonthlyScheduleResponse getMonthlyScheduleByStaff(int staffId, int month, int year);
    
    /**
     * Lấy lịch trong một ngày cụ thể
     * @param date ngày cần xem
     * @return lịch theo ngày với tất cả staff
     */
    DailyScheduleResponse getDailySchedule(Date date);
    
    /**
     * Lấy lịch trong một ngày của một staff cụ thể
     * @param staffId ID của staff
     * @param date ngày cần xem
     * @return lịch theo ngày
     */
    DailyScheduleResponse getDailyScheduleByStaff(int staffId, Date date);

    // ==================== UTILITY ====================
    
    /**
     * Kiểm tra xem có thể chỉnh sửa lịch của ngày này không (không cho sửa ngày quá khứ)
     * @param date ngày cần kiểm tra
     * @return true nếu có thể sửa
     */
    boolean canModifySchedule(Date date);
    
    /**
     * Lấy danh sách staff có lịch trong tháng
     * @param month tháng
     * @param year năm
     * @return danh sách staffId
     */
    List<Integer> getStaffWithScheduleInMonth(int month, int year);
}
