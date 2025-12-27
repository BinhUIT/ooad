package com.example.ooad.service.staff;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EScheduleStatus;
import com.example.ooad.dto.request.BulkShiftAssignmentRequest;
import com.example.ooad.dto.request.CopyScheduleFromPreviousMonthRequest;
import com.example.ooad.dto.request.RecurringScheduleRequest;
import com.example.ooad.dto.request.ShiftAssignmentRequest;
import com.example.ooad.dto.request.StaffScheduleRequest;
import com.example.ooad.dto.response.BulkOperationResponse;
import com.example.ooad.dto.response.DailyScheduleResponse;
import com.example.ooad.dto.response.MonthlyScheduleResponse;
import com.example.ooad.dto.response.ScheduleSlotResponse;
import com.example.ooad.dto.response.ShiftResponse;
import com.example.ooad.dto.response.TimeSlotWithAppointmentResponse;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.repository.StaffScheduleRepository;

@Service
public class StaffScheduleServiceImpl implements StaffScheduleService {

    private final StaffScheduleRepository scheduleRepository;
    private final StaffRepository staffRepository;
    private final AppointmentRepository appointmentRepository;

    // Định nghĩa các time slots chuẩn
    private static final LocalTime[] MORNING_SLOTS_START = {
        LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0)
    };
    private static final LocalTime[] AFTERNOON_SLOTS_START = {
        LocalTime.of(13, 0), LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(16, 0)
    };

    // Màu sắc cho staff (dùng cho calendar view)
    private static final String[] STAFF_COLORS = {
        "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FFEAA7", 
        "#DDA0DD", "#98D8C8", "#F7DC6F", "#BB8FCE", "#85C1E9"
    };

    public StaffScheduleServiceImpl(StaffScheduleRepository scheduleRepository, 
                                   StaffRepository staffRepository,
                                   AppointmentRepository appointmentRepository) {
        this.scheduleRepository = scheduleRepository;
        this.staffRepository = staffRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // ==================== BASIC CRUD ====================

    @Override
    @Transactional
    public ScheduleSlotResponse createSlot(StaffScheduleRequest request) {
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new NotFoundException("Staff not found with id: " + request.getStaffId()));

        // Kiểm tra không được tạo lịch cho ngày quá khứ
        if (!canModifySchedule(request.getScheduleDate())) {
            throw new IllegalArgumentException("Cannot create schedule for past dates");
        }

        // Kiểm tra slot đã tồn tại chưa
        if (scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                request.getStaffId(), request.getScheduleDate(), request.getStartTime())) {
            throw new IllegalArgumentException("Schedule slot already exists for this staff, date and time");
        }

        StaffSchedule schedule = new StaffSchedule();
        schedule.setStaff(staff);
        schedule.setScheduleDate(request.getScheduleDate());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setStatus(request.getStatus() != null ? request.getStatus() : EScheduleStatus.AVAILABLE);

        StaffSchedule saved = scheduleRepository.save(schedule);
        return new ScheduleSlotResponse(saved);
    }

    @Override
    public ScheduleSlotResponse getSlotById(int scheduleId) {
        StaffSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule not found with id: " + scheduleId));
        
        return new ScheduleSlotResponse(schedule);
    }

    @Override
    @Transactional
    public ScheduleSlotResponse updateSlot(int scheduleId, StaffScheduleRequest request) {
        StaffSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule not found with id: " + scheduleId));

        // Kiểm tra không được sửa lịch ngày quá khứ
        if (!canModifySchedule(schedule.getScheduleDate())) {
            throw new IllegalArgumentException("Cannot modify schedule for past dates");
        }

        // Cập nhật thông tin
        if (request.getStatus() != null) {
            schedule.setStatus(request.getStatus());
        }
        if (request.getStartTime() != null) {
            schedule.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            schedule.setEndTime(request.getEndTime());
        }

        StaffSchedule saved = scheduleRepository.save(schedule);
        return new ScheduleSlotResponse(saved);
    }

    @Override
    @Transactional
    public void deleteSlot(int scheduleId) {
        StaffSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule not found with id: " + scheduleId));

        // Kiểm tra không được xóa lịch ngày quá khứ
        if (!canModifySchedule(schedule.getScheduleDate())) {
            throw new IllegalArgumentException("Cannot delete schedule for past dates");
        }
        
        scheduleRepository.delete(schedule);
    }

    // ==================== SHIFT ASSIGNMENT ====================

    @Override
    @Transactional
    public List<ScheduleSlotResponse> assignShift(ShiftAssignmentRequest request) {
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new NotFoundException("Staff not found with id: " + request.getStaffId()));

        // Kiểm tra không được tạo lịch cho ngày quá khứ
        if (!canModifySchedule(request.getScheduleDate())) {
            throw new IllegalArgumentException("Cannot create schedule for past dates");
        }

        LocalTime[] slotStarts = request.getShiftType() == ShiftAssignmentRequest.ShiftType.MORNING 
                ? MORNING_SLOTS_START 
                : AFTERNOON_SLOTS_START;

        List<ScheduleSlotResponse> createdSlots = new ArrayList<>();
        
        for (LocalTime startTime : slotStarts) {
            LocalTime endTime = startTime.plusHours(1);
            
            // Kiểm tra slot đã tồn tại chưa - skip nếu đã có
            if (scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                    request.getStaffId(), request.getScheduleDate(), startTime)) {
                continue;
            }
            
            // Tạo slot mới
            StaffSchedule schedule = new StaffSchedule();
            schedule.setStaff(staff);
            schedule.setScheduleDate(request.getScheduleDate());
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setStatus(request.getStatus() != null ? request.getStatus() : EScheduleStatus.AVAILABLE);

            StaffSchedule saved = scheduleRepository.save(schedule);
            createdSlots.add(new ScheduleSlotResponse(saved));
        }

        return createdSlots;
    }

    @Override
    @Transactional
    public BulkOperationResponse bulkAssignShifts(BulkShiftAssignmentRequest request) {
        int successCount = 0;
        int skippedCount = 0;
        int errorCount = 0;
        List<BulkOperationResponse.ConflictInfo> conflicts = new ArrayList<>();
        List<Date> createdDates = new ArrayList<>();

        for (Date date : request.getScheduleDates()) {
            ShiftAssignmentRequest shiftRequest = new ShiftAssignmentRequest();
            shiftRequest.setStaffId(request.getStaffId());
            shiftRequest.setScheduleDate(date);
            shiftRequest.setShiftType(request.getShiftType());
            shiftRequest.setStatus(request.getStatus());

            try {
                List<ScheduleSlotResponse> slots = assignShift(shiftRequest);
                if (slots.isEmpty()) {
                    skippedCount++;
                } else {
                    successCount += slots.size();
                    createdDates.add(date);
                }
            } catch (Exception e) {
                errorCount++;
                BulkOperationResponse.ConflictInfo conflictInfo = new BulkOperationResponse.ConflictInfo();
                conflictInfo.setDate(date);
                conflictInfo.setStaffId(request.getStaffId());
                conflictInfo.setShiftType(request.getShiftType().name());
                conflictInfo.setExistingShiftInfo(e.getMessage());
                conflicts.add(conflictInfo);
            }
        }

        BulkOperationResponse response = new BulkOperationResponse();
        response.setSuccess(errorCount == 0);
        response.setSuccessCount(successCount);
        response.setSkippedCount(skippedCount);
        response.setErrorCount(errorCount);
        response.setConflicts(conflicts);
        response.setCreatedDates(createdDates);
        response.setMessage(String.format("Bulk assignment completed: %d slots created, %d skipped, %d failed", 
                successCount, skippedCount, errorCount));

        return response;
    }

    // ==================== RECURRING SCHEDULE ====================

    @Override
    @Transactional
    public BulkOperationResponse createRecurringSchedule(RecurringScheduleRequest request) {
        int successCount = 0;
        int skippedCount = 0;
        int errorCount = 0;
        List<BulkOperationResponse.ConflictInfo> conflicts = new ArrayList<>();
        List<Date> createdDates = new ArrayList<>();

        LocalDate startDate = request.getStartDate().toLocalDate();
        LocalDate endDate = request.getEndDate().toLocalDate();
        List<Integer> daysOfWeek = request.getDaysOfWeek();
        
        // Convert daysOfWeek list to Set of DayOfWeek for quick lookup
        java.util.Set<DayOfWeek> targetDays = new java.util.HashSet<>();
        for (Integer day : daysOfWeek) {
            targetDays.add(DayOfWeek.of(day)); // 1=Monday, 7=Sunday (ISO-8601)
        }
        
        // Duyệt qua từng ngày từ startDate đến endDate
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (targetDays.contains(currentDate.getDayOfWeek())) {
                Date sqlDate = Date.valueOf(currentDate);
                
                // Bỏ qua ngày quá khứ
                if (canModifySchedule(sqlDate)) {
                    ShiftAssignmentRequest shiftRequest = new ShiftAssignmentRequest();
                    shiftRequest.setStaffId(request.getStaffId());
                    shiftRequest.setDate(sqlDate);
                    shiftRequest.setShiftType(request.getShiftType());
                    shiftRequest.setStatus(request.getStatus());
                    shiftRequest.setConflictAction(request.getConflictAction());

                    try {
                        List<ScheduleSlotResponse> slots = assignShift(shiftRequest);
                        if (slots.isEmpty()) {
                            skippedCount++;
                            if (request.getConflictAction() == ShiftAssignmentRequest.ConflictAction.CANCEL) {
                                throw new IllegalArgumentException("Conflict found and action is CANCEL");
                            }
                        } else {
                            successCount += slots.size();
                            createdDates.add(sqlDate);
                        }
                    } catch (Exception e) {
                        if (request.getConflictAction() == ShiftAssignmentRequest.ConflictAction.CANCEL) {
                            throw new IllegalArgumentException("Conflict found: " + e.getMessage());
                        }
                        errorCount++;
                        BulkOperationResponse.ConflictInfo conflictInfo = new BulkOperationResponse.ConflictInfo();
                        conflictInfo.setDate(sqlDate);
                        conflictInfo.setStaffId(request.getStaffId());
                        conflictInfo.setShiftType(request.getShiftType().name());
                        conflictInfo.setExistingShiftInfo(e.getMessage());
                        conflicts.add(conflictInfo);
                    }
                }
            }
            
            currentDate = currentDate.plusDays(1);
        }

        BulkOperationResponse response = new BulkOperationResponse();
        response.setSuccess(errorCount == 0);
        response.setSuccessCount(successCount);
        response.setSkippedCount(skippedCount);
        response.setErrorCount(errorCount);
        response.setConflicts(conflicts);
        response.setCreatedDates(createdDates);
        response.setMessage(String.format("Recurring schedule created: %d slots created, %d skipped, %d conflicts", 
                successCount, skippedCount, conflicts.size()));

        return response;
    }

    // ==================== COPY FROM PREVIOUS MONTH ====================

    @Override
    @Transactional
    public BulkOperationResponse copyFromPreviousMonth(CopyScheduleFromPreviousMonthRequest request) {
        int sourceMonth = request.getSourceMonth();
        int sourceYear = request.getSourceYear();
        int targetMonth = request.getTargetMonth();
        int targetYear = request.getTargetYear();

        // Kiểm tra tháng đích không phải là quá khứ
        YearMonth targetYearMonth = YearMonth.of(targetYear, targetMonth);
        LocalDate targetFirstDay = targetYearMonth.atDay(1);
        if (targetFirstDay.isBefore(LocalDate.now()) && !targetYearMonth.equals(YearMonth.now())) {
            throw new IllegalArgumentException("Cannot copy to a past month");
        }

        // Lấy lịch từ tháng nguồn
        List<StaffSchedule> sourceSchedules;
        if (request.getStaffId() != null) {
            sourceSchedules = scheduleRepository.findByStaffAndMonth(request.getStaffId(), sourceMonth, sourceYear);
        } else {
            sourceSchedules = scheduleRepository.findByMonth(sourceMonth, sourceYear);
        }

        if (sourceSchedules.isEmpty()) {
            BulkOperationResponse response = new BulkOperationResponse();
            response.setSuccess(true);
            response.setSuccessCount(0);
            response.setSkippedCount(0);
            response.setErrorCount(0);
            response.setConflicts(new ArrayList<>());
            response.setCreatedDates(new ArrayList<>());
            response.setMessage("No schedules found in source month");
            return response;
        }

        int successCount = 0;
        int skippedCount = 0;
        int errorCount = 0;
        List<BulkOperationResponse.ConflictInfo> conflicts = new ArrayList<>();
        List<Date> createdDates = new ArrayList<>();

        // Group by staff và dayOfMonth để copy
        Map<Integer, Map<Integer, List<StaffSchedule>>> groupedSchedules = sourceSchedules.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getStaff().getStaffId(),
                        Collectors.groupingBy(s -> {
                            LocalDate date = s.getScheduleDate().toLocalDate();
                            return date.getDayOfMonth();
                        })
                ));

        // Số ngày trong tháng đích
        int daysInTargetMonth = targetYearMonth.lengthOfMonth();

        for (Map.Entry<Integer, Map<Integer, List<StaffSchedule>>> staffEntry : groupedSchedules.entrySet()) {
            int staffId = staffEntry.getKey();
            Staff staff = staffRepository.findById(staffId).orElse(null);
            if (staff == null) continue;

            for (Map.Entry<Integer, List<StaffSchedule>> dayEntry : staffEntry.getValue().entrySet()) {
                int dayOfMonth = dayEntry.getKey();
                
                // Bỏ qua nếu ngày không tồn tại trong tháng đích
                if (dayOfMonth > daysInTargetMonth) continue;

                LocalDate targetDate = LocalDate.of(targetYear, targetMonth, dayOfMonth);
                Date sqlTargetDate = Date.valueOf(targetDate);

                // Bỏ qua ngày quá khứ
                if (!canModifySchedule(sqlTargetDate)) continue;

                for (StaffSchedule sourceSlot : dayEntry.getValue()) {
                    // Kiểm tra conflict
                    boolean exists = scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                            staffId, sqlTargetDate, sourceSlot.getStartTime());

                    if (exists) {
                        ShiftAssignmentRequest.ConflictAction action = request.getConflictAction();
                        if (action == ShiftAssignmentRequest.ConflictAction.SKIP) {
                            skippedCount++;
                            continue;
                        } else if (action == ShiftAssignmentRequest.ConflictAction.CANCEL) {
                            errorCount++;
                            BulkOperationResponse.ConflictInfo conflictInfo = new BulkOperationResponse.ConflictInfo();
                            conflictInfo.setDate(sqlTargetDate);
                            conflictInfo.setStaffId(staffId);
                            conflictInfo.setStaffName(staff.getFullName());
                            conflictInfo.setExistingShiftInfo("Slot already exists");
                            conflicts.add(conflictInfo);
                            continue;
                        }
                        // OVERWRITE: xóa slot cũ
                        Optional<StaffSchedule> existingSlot = scheduleRepository
                                .findByStaff_StaffIdAndScheduleDateAndStartTime(staffId, sqlTargetDate, sourceSlot.getStartTime());
                        existingSlot.ifPresent(scheduleRepository::delete);
                    }

                    // Tạo slot mới
                    StaffSchedule newSlot = new StaffSchedule();
                    newSlot.setStaff(staff);
                    newSlot.setScheduleDate(sqlTargetDate);
                    newSlot.setStartTime(sourceSlot.getStartTime());
                    newSlot.setEndTime(sourceSlot.getEndTime());
                    newSlot.setStatus(EScheduleStatus.AVAILABLE);

                    try {
                        scheduleRepository.save(newSlot);
                        if (!createdDates.contains(sqlTargetDate)) {
                            createdDates.add(sqlTargetDate);
                        }
                        successCount++;
                    } catch (Exception e) {
                        errorCount++;
                    }
                }
            }
        }

        BulkOperationResponse response = new BulkOperationResponse();
        response.setSuccess(errorCount == 0);
        response.setSuccessCount(successCount);
        response.setSkippedCount(skippedCount);
        response.setErrorCount(errorCount);
        response.setConflicts(conflicts);
        response.setCreatedDates(createdDates);
        response.setMessage(String.format("Copied %d schedules from %d/%d to %d/%d. %d conflicts.", 
                successCount, sourceMonth, sourceYear, targetMonth, targetYear, conflicts.size()));

        return response;
    }

    // ==================== VIEW SCHEDULES ====================

    @Override
    public MonthlyScheduleResponse getMonthlySchedule(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        List<StaffSchedule> schedules = scheduleRepository.findByScheduleDateBetweenOrderByScheduleDateAscStartTimeAsc(
                Date.valueOf(firstDay), Date.valueOf(lastDay));

        return buildMonthlyScheduleResponse(schedules, month, year);
    }

    @Override
    public MonthlyScheduleResponse getMonthlyScheduleByStaff(int staffId, int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        List<StaffSchedule> schedules = scheduleRepository
                .findByStaff_StaffIdAndScheduleDateBetweenOrderByScheduleDateAscStartTimeAsc(
                        staffId, Date.valueOf(firstDay), Date.valueOf(lastDay));

        return buildMonthlyScheduleResponse(schedules, month, year);
    }

    @Override
    public DailyScheduleResponse getDailySchedule(Date date) {
        List<StaffSchedule> schedules = scheduleRepository.findByScheduleDateOrderByStaff_FullNameAscStartTimeAsc(date);
        return buildDailyScheduleResponse(schedules, date);
    }

    @Override
    public DailyScheduleResponse getDailyScheduleByStaff(int staffId, Date date) {
        List<StaffSchedule> schedules = scheduleRepository.findByStaff_StaffIdAndScheduleDateOrderByStartTimeAsc(staffId, date);
        return buildDailyScheduleResponse(schedules, date);
    }

    // ==================== UTILITY ====================

    @Override
    public boolean canModifySchedule(Date date) {
        LocalDate scheduleDate = date.toLocalDate();
        LocalDate today = LocalDate.now();
        return !scheduleDate.isBefore(today);
    }

    @Override
    public List<Integer> getStaffWithScheduleInMonth(int month, int year) {
        return scheduleRepository.findDistinctStaffIdsByMonth(month, year);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private MonthlyScheduleResponse buildMonthlyScheduleResponse(List<StaffSchedule> schedules, int month, int year) {
        MonthlyScheduleResponse response = new MonthlyScheduleResponse();
        response.setMonth(month);
        response.setYear(year);

        // Check if current month or past month
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth targetYearMonth = YearMonth.of(year, month);
        response.setCurrentMonth(currentYearMonth.equals(targetYearMonth));
        response.setPastMonth(targetYearMonth.isBefore(currentYearMonth));

        // Build staff colors list
        Map<Integer, String> staffColorMap = new HashMap<>();
        Set<Integer> staffIds = schedules.stream()
                .map(s -> s.getStaff().getStaffId())
                .collect(Collectors.toSet());
        
        int colorIndex = 0;
        List<MonthlyScheduleResponse.StaffColorMapping> staffColorsList = new ArrayList<>();
        for (Integer staffId : staffIds) {
            String color = STAFF_COLORS[colorIndex % STAFF_COLORS.length];
            staffColorMap.put(staffId, color);
            
            // Get staff name
            String staffName = schedules.stream()
                    .filter(s -> s.getStaff().getStaffId() == staffId)
                    .findFirst()
                    .map(s -> s.getStaff().getFullName())
                    .orElse("Unknown");
            
            MonthlyScheduleResponse.StaffColorMapping mapping = new MonthlyScheduleResponse.StaffColorMapping();
            mapping.setStaffId(staffId);
            mapping.setStaffName(staffName);
            mapping.setColor(color);
            staffColorsList.add(mapping);
            
            colorIndex++;
        }
        response.setStaffColors(staffColorsList);

        // Group by date
        Map<Integer, List<MonthlyScheduleResponse.DayScheduleSummary>> scheduleByDay = new LinkedHashMap<>();
        Map<Date, List<StaffSchedule>> groupedByDate = schedules.stream()
                .collect(Collectors.groupingBy(StaffSchedule::getScheduleDate));

        int totalShifts = 0;
        for (Map.Entry<Date, List<StaffSchedule>> entry : groupedByDate.entrySet()) {
            int dayOfMonth = entry.getKey().toLocalDate().getDayOfMonth();
            List<StaffSchedule> daySchedules = entry.getValue();
            
            // Group by staff for this day
            Map<Integer, List<StaffSchedule>> groupedByStaff = daySchedules.stream()
                    .collect(Collectors.groupingBy(s -> s.getStaff().getStaffId()));
            
            List<MonthlyScheduleResponse.DayScheduleSummary> daySummaries = new ArrayList<>();
            for (Map.Entry<Integer, List<StaffSchedule>> staffEntry : groupedByStaff.entrySet()) {
                int staffId = staffEntry.getKey();
                List<StaffSchedule> staffSlots = staffEntry.getValue();
                
                // Determine if this is morning or afternoon shift
                boolean hasMorning = staffSlots.stream()
                        .anyMatch(s -> s.getStartTime().isBefore(LocalTime.of(12, 0)));
                boolean hasAfternoon = staffSlots.stream()
                        .anyMatch(s -> !s.getStartTime().isBefore(LocalTime.of(13, 0)));
                
                String shiftType = hasMorning && hasAfternoon ? "FULL_DAY" : 
                                   hasMorning ? "MORNING" : "AFTERNOON";
                
                MonthlyScheduleResponse.DayScheduleSummary summary = new MonthlyScheduleResponse.DayScheduleSummary();
                summary.setStaffId(staffId);
                summary.setStaffName(staffSlots.get(0).getStaff().getFullName());
                summary.setStaffColor(staffColorMap.get(staffId));
                summary.setShiftType(shiftType);
                summary.setSlotCount(staffSlots.size());
                
                daySummaries.add(summary);
                totalShifts++;
            }
            
            scheduleByDay.put(dayOfMonth, daySummaries);
        }

        response.setScheduleByDay(scheduleByDay);
        response.setTotalShifts(totalShifts);

        // Check if previous month has schedules
        YearMonth prevMonth = targetYearMonth.minusMonths(1);
        long prevMonthCount = scheduleRepository.findByMonth(prevMonth.getMonthValue(), prevMonth.getYear()).size();
        response.setHasPreviousMonthSchedule(prevMonthCount > 0);

        return response;
    }

    private DailyScheduleResponse buildDailyScheduleResponse(List<StaffSchedule> schedules, Date date) {
        DailyScheduleResponse response = new DailyScheduleResponse();
        response.setDate(date);
        
        LocalDate localDate = date.toLocalDate();
        LocalDate today = LocalDate.now();
        response.setPastDate(localDate.isBefore(today));
        response.setToday(localDate.equals(today));
        response.setDayOfWeek(localDate.getDayOfWeek().getValue());
        response.setDayOfWeekName(localDate.getDayOfWeek().name());

        // Group by staff
        Map<Integer, List<StaffSchedule>> groupedByStaff = schedules.stream()
                .collect(Collectors.groupingBy(s -> s.getStaff().getStaffId()));

        List<ShiftResponse> shifts = new ArrayList<>();

        for (Map.Entry<Integer, List<StaffSchedule>> entry : groupedByStaff.entrySet()) {
            List<StaffSchedule> staffSlots = entry.getValue();
            
            if (staffSlots.isEmpty()) continue;
            
            Staff staff = staffSlots.get(0).getStaff();

            // Phân loại slots theo ca sáng/chiều
            List<StaffSchedule> morningSlots = staffSlots.stream()
                    .filter(s -> s.getStartTime().isBefore(LocalTime.of(12, 0)))
                    .collect(Collectors.toList());
            
            List<StaffSchedule> afternoonSlots = staffSlots.stream()
                    .filter(s -> !s.getStartTime().isBefore(LocalTime.of(13, 0)))
                    .collect(Collectors.toList());

            // Tạo ShiftResponse cho ca sáng nếu có
            if (!morningSlots.isEmpty()) {
                ShiftResponse morningShift = new ShiftResponse();
                morningShift.setStaffId(staff.getStaffId());
                morningShift.setStaffName(staff.getFullName());
                morningShift.setStaffPosition(staff.getPosition());
                morningShift.setScheduleDate(date);
                morningShift.setShiftType(ShiftAssignmentRequest.ShiftType.MORNING);
                morningShift.setStartTime(LocalTime.of(8, 0));
                morningShift.setEndTime(LocalTime.of(12, 0));
                
                // Build time slots with appointment info
                List<TimeSlotWithAppointmentResponse> timeSlots = buildTimeSlotsWithAppointments(morningSlots, staff.getStaffId(), date);
                morningShift.setTimeSlots(timeSlots);
                
                // Count booked slots and determine shift status
                int bookedCount = (int) timeSlots.stream().filter(ts -> ts.getAppointmentId() != null).count();
                morningShift.setBookedSlotsCount(bookedCount);
                morningShift.setTotalSlotsCount(morningSlots.size());
                
                // Set status based on bookings
                if (bookedCount == 0) {
                    morningShift.setStatus(EScheduleStatus.AVAILABLE);
                } else if (bookedCount == morningSlots.size()) {
                    morningShift.setStatus(EScheduleStatus.BOOKED);
                } else {
                    morningShift.setStatus(EScheduleStatus.AVAILABLE); // Partially booked still shows as available
                }
                
                shifts.add(morningShift);
            }

            // Tạo ShiftResponse cho ca chiều nếu có
            if (!afternoonSlots.isEmpty()) {
                ShiftResponse afternoonShift = new ShiftResponse();
                afternoonShift.setStaffId(staff.getStaffId());
                afternoonShift.setStaffName(staff.getFullName());
                afternoonShift.setStaffPosition(staff.getPosition());
                afternoonShift.setScheduleDate(date);
                afternoonShift.setShiftType(ShiftAssignmentRequest.ShiftType.AFTERNOON);
                afternoonShift.setStartTime(LocalTime.of(13, 0));
                afternoonShift.setEndTime(LocalTime.of(17, 0));
                
                // Build time slots with appointment info
                List<TimeSlotWithAppointmentResponse> timeSlots = buildTimeSlotsWithAppointments(afternoonSlots, staff.getStaffId(), date);
                afternoonShift.setTimeSlots(timeSlots);
                
                // Count booked slots and determine shift status
                int bookedCount = (int) timeSlots.stream().filter(ts -> ts.getAppointmentId() != null).count();
                afternoonShift.setBookedSlotsCount(bookedCount);
                afternoonShift.setTotalSlotsCount(afternoonSlots.size());
                
                // Set status based on bookings
                if (bookedCount == 0) {
                    afternoonShift.setStatus(EScheduleStatus.AVAILABLE);
                } else if (bookedCount == afternoonSlots.size()) {
                    afternoonShift.setStatus(EScheduleStatus.BOOKED);
                } else {
                    afternoonShift.setStatus(EScheduleStatus.AVAILABLE); // Partially booked still shows as available
                }
                
                shifts.add(afternoonShift);
            }
        }

        response.setShifts(shifts);
        response.setTotalShifts(shifts.size());

        return response;
    }
    
    /**
     * Helper method to build time slots with appointment and patient information
     */
    private List<TimeSlotWithAppointmentResponse> buildTimeSlotsWithAppointments(
            List<StaffSchedule> slots, int staffId, Date date) {
        
        List<TimeSlotWithAppointmentResponse> timeSlotResponses = new ArrayList<>();
        
        for (StaffSchedule slot : slots) {
            TimeSlotWithAppointmentResponse timeSlot = new TimeSlotWithAppointmentResponse();
            timeSlot.setStaffScheduleId(slot.getScheduleId());
            timeSlot.setStartTime(slot.getStartTime());
            timeSlot.setEndTime(slot.getEndTime());
            
            // Find appointment for this time slot
            Optional<Appointment> appointment = appointmentRepository
                    .findByStaff_StaffIdAndAppointmentDateAndAppointmentTime(
                            staffId, date, slot.getStartTime());
            
            if (appointment.isPresent()) {
                Appointment appt = appointment.get();
                timeSlot.setStatus(EScheduleStatus.BOOKED);
                timeSlot.setAppointmentId(appt.getAppointmentId());
                timeSlot.setAppointmentStatus(appt.getStatus().name());
                
                if (appt.getPatient() != null) {
                    timeSlot.setPatientId(appt.getPatient().getPatientId());
                    timeSlot.setPatientName(appt.getPatient().getFullName());
                }
            } else {
                timeSlot.setStatus(slot.getStatus());
                timeSlot.setAppointmentId(null);
                timeSlot.setAppointmentStatus(null);
                timeSlot.setPatientId(null);
                timeSlot.setPatientName(null);
            }
            
            timeSlotResponses.add(timeSlot);
        }
        
        return timeSlotResponses;
    }
}
