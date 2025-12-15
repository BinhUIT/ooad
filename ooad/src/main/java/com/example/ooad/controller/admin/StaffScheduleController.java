package com.example.ooad.controller.admin;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.request.BulkShiftAssignmentRequest;
import com.example.ooad.dto.request.CopyScheduleFromPreviousMonthRequest;
import com.example.ooad.dto.request.RecurringScheduleRequest;
import com.example.ooad.dto.request.ShiftAssignmentRequest;
import com.example.ooad.dto.request.StaffScheduleRequest;
import com.example.ooad.dto.response.BulkOperationResponse;
import com.example.ooad.dto.response.DailyScheduleResponse;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.MonthlyScheduleResponse;
import com.example.ooad.dto.response.ScheduleSlotResponse;
import com.example.ooad.service.staff.StaffScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller quản lý lịch làm việc của nhân viên (bác sĩ).
 * Chỉ ADMIN mới có quyền truy cập.
 */
@RestController
@RequestMapping("/admin/schedules")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Staff Schedule Management", description = "APIs quản lý lịch làm việc của bác sĩ")
public class StaffScheduleController {

    private final StaffScheduleService scheduleService;

    public StaffScheduleController(StaffScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // ==================== BASIC CRUD ====================

    @Operation(summary = "Tạo một time slot đơn lẻ", 
               description = "Tạo một slot 1 tiếng cho bác sĩ. Không được tạo cho ngày quá khứ.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tạo thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc slot đã tồn tại"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy staff")
    })
    @PostMapping
    public ResponseEntity<GlobalResponse<ScheduleSlotResponse>> createSlot(
            @Valid @RequestBody StaffScheduleRequest request) {
        ScheduleSlotResponse response = scheduleService.createSlot(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(response, "Schedule slot created successfully", HttpStatus.CREATED.value()));
    }

    @Operation(summary = "Lấy thông tin chi tiết một slot")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<GlobalResponse<ScheduleSlotResponse>> getSlotById(
            @Parameter(description = "ID của slot") @PathVariable int scheduleId) {
        ScheduleSlotResponse response = scheduleService.getSlotById(scheduleId);
        return ResponseEntity.ok(new GlobalResponse<>(response, "Success", HttpStatus.OK.value()));
    }

    @Operation(summary = "Cập nhật trạng thái slot", 
               description = "Cập nhật status của slot. Không được sửa ngày quá khứ.")
    @PutMapping("/{scheduleId}")
    public ResponseEntity<GlobalResponse<ScheduleSlotResponse>> updateSlot(
            @Parameter(description = "ID của slot") @PathVariable int scheduleId,
            @Valid @RequestBody StaffScheduleRequest request) {
        ScheduleSlotResponse response = scheduleService.updateSlot(scheduleId, request);
        return ResponseEntity.ok(new GlobalResponse<>(response, "Schedule slot updated successfully", HttpStatus.OK.value()));
    }

    @Operation(summary = "Xóa một slot", 
               description = "Xóa slot. Không được xóa ngày quá khứ hoặc slot đã có appointment.")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<GlobalResponse<Void>> deleteSlot(
            @Parameter(description = "ID của slot") @PathVariable int scheduleId) {
        scheduleService.deleteSlot(scheduleId);
        return ResponseEntity.ok(new GlobalResponse<>(null, "Schedule slot deleted successfully", HttpStatus.OK.value()));
    }

    // ==================== SHIFT ASSIGNMENT ====================

    @Operation(summary = "Phân công một ca làm việc (4 slots)", 
               description = "Tạo 4 time slots liên tiếp cho ca MORNING (8-12h) hoặc AFTERNOON (13-17h)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Phân ca thành công"),
        @ApiResponse(responseCode = "400", description = "Có conflict và conflictAction=CANCEL")
    })
    @PostMapping("/shift")
    public ResponseEntity<GlobalResponse<List<ScheduleSlotResponse>>> assignShift(
            @Valid @RequestBody ShiftAssignmentRequest request) {
        List<ScheduleSlotResponse> response = scheduleService.assignShift(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(response, "Shift assigned successfully", HttpStatus.CREATED.value()));
    }

    @Operation(summary = "Phân công nhiều ca cùng lúc (bulk)", 
               description = "Phân công cùng một loại ca cho nhiều ngày khác nhau")
    @PostMapping("/bulk")
    public ResponseEntity<GlobalResponse<BulkOperationResponse>> bulkAssignShifts(
            @Valid @RequestBody BulkShiftAssignmentRequest request) {
        BulkOperationResponse response = scheduleService.bulkAssignShifts(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(response, response.getMessage(), HttpStatus.CREATED.value()));
    }

    // ==================== RECURRING SCHEDULE ====================

    @Operation(summary = "Tạo lịch định kỳ hàng tuần", 
               description = "Tạo lịch lặp lại theo các ngày trong tuần (vd: thứ 2, 4, 6 hàng tuần)")
    @PostMapping("/recurring")
    public ResponseEntity<GlobalResponse<BulkOperationResponse>> createRecurringSchedule(
            @Valid @RequestBody RecurringScheduleRequest request) {
        BulkOperationResponse response = scheduleService.createRecurringSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(response, response.getMessage(), HttpStatus.CREATED.value()));
    }

    // ==================== COPY FROM PREVIOUS MONTH ====================

    @Operation(summary = "Copy lịch từ tháng trước", 
               description = "Copy toàn bộ hoặc một phần lịch từ tháng nguồn sang tháng đích")
    @PostMapping("/copy-from-previous")
    public ResponseEntity<GlobalResponse<BulkOperationResponse>> copyFromPreviousMonth(
            @Valid @RequestBody CopyScheduleFromPreviousMonthRequest request) {
        BulkOperationResponse response = scheduleService.copyFromPreviousMonth(request);
        return ResponseEntity.ok(new GlobalResponse<>(response, response.getMessage(), HttpStatus.OK.value()));
    }

    // ==================== VIEW SCHEDULES ====================

    @Operation(summary = "Xem lịch theo tháng (tất cả staff)", 
               description = "Lấy lịch của tất cả staff trong tháng, dạng calendar view với legend màu")
    @GetMapping("/monthly")
    public ResponseEntity<GlobalResponse<MonthlyScheduleResponse>> getMonthlySchedule(
            @Parameter(description = "Tháng (1-12)") @RequestParam int month,
            @Parameter(description = "Năm") @RequestParam int year) {
        MonthlyScheduleResponse response = scheduleService.getMonthlySchedule(month, year);
        return ResponseEntity.ok(new GlobalResponse<>(response, "Success", HttpStatus.OK.value()));
    }

    @Operation(summary = "Xem lịch theo tháng của một staff", 
               description = "Lấy lịch của một bác sĩ cụ thể trong tháng")
    @GetMapping("/monthly/staff/{staffId}")
    public ResponseEntity<GlobalResponse<MonthlyScheduleResponse>> getMonthlyScheduleByStaff(
            @Parameter(description = "ID của staff") @PathVariable int staffId,
            @Parameter(description = "Tháng (1-12)") @RequestParam int month,
            @Parameter(description = "Năm") @RequestParam int year) {
        MonthlyScheduleResponse response = scheduleService.getMonthlyScheduleByStaff(staffId, month, year);
        return ResponseEntity.ok(new GlobalResponse<>(response, "Success", HttpStatus.OK.value()));
    }

    @Operation(summary = "Xem lịch theo ngày (tất cả staff)", 
               description = "Lấy lịch của tất cả staff trong một ngày cụ thể")
    @GetMapping("/daily")
    public ResponseEntity<GlobalResponse<DailyScheduleResponse>> getDailySchedule(
            @Parameter(description = "Ngày (yyyy-MM-dd)") @RequestParam String date) {
        Date sqlDate = Date.valueOf(LocalDate.parse(date));
        DailyScheduleResponse response = scheduleService.getDailySchedule(sqlDate);
        return ResponseEntity.ok(new GlobalResponse<>(response, "Success", HttpStatus.OK.value()));
    }

    @Operation(summary = "Xem lịch theo ngày của một staff", 
               description = "Lấy lịch của một bác sĩ cụ thể trong một ngày")
    @GetMapping("/daily/staff/{staffId}")
    public ResponseEntity<GlobalResponse<DailyScheduleResponse>> getDailyScheduleByStaff(
            @Parameter(description = "ID của staff") @PathVariable int staffId,
            @Parameter(description = "Ngày (yyyy-MM-dd)") @RequestParam String date) {
        Date sqlDate = Date.valueOf(LocalDate.parse(date));
        DailyScheduleResponse response = scheduleService.getDailyScheduleByStaff(staffId, sqlDate);
        return ResponseEntity.ok(new GlobalResponse<>(response, "Success", HttpStatus.OK.value()));
    }

    // ==================== UTILITY ====================

    @Operation(summary = "Lấy danh sách staff có lịch trong tháng", 
               description = "Trả về danh sách staffId có lịch làm việc trong tháng")
    @GetMapping("/staff-list")
    public ResponseEntity<GlobalResponse<List<Integer>>> getStaffWithScheduleInMonth(
            @Parameter(description = "Tháng (1-12)") @RequestParam int month,
            @Parameter(description = "Năm") @RequestParam int year) {
        List<Integer> staffIds = scheduleService.getStaffWithScheduleInMonth(month, year);
        return ResponseEntity.ok(new GlobalResponse<>(staffIds, "Success", HttpStatus.OK.value()));
    }

    @Operation(summary = "Kiểm tra có thể sửa lịch ngày này không", 
               description = "Trả về true nếu ngày chưa qua, false nếu đã qua")
    @GetMapping("/can-modify")
    public ResponseEntity<GlobalResponse<Boolean>> canModifySchedule(
            @Parameter(description = "Ngày (yyyy-MM-dd)") @RequestParam String date) {
        Date sqlDate = Date.valueOf(LocalDate.parse(date));
        boolean canModify = scheduleService.canModifySchedule(sqlDate);
        return ResponseEntity.ok(new GlobalResponse<>(canModify, canModify ? "Can modify" : "Cannot modify past date", HttpStatus.OK.value()));
    }
}
