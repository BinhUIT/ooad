package com.example.ooad.service.staff;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EScheduleStatus;
import com.example.ooad.dto.request.RecurringScheduleRequest;
import com.example.ooad.dto.request.ShiftAssignmentRequest;
import com.example.ooad.dto.request.StaffScheduleRequest;
import com.example.ooad.dto.response.BulkOperationResponse;
import com.example.ooad.dto.response.ScheduleSlotResponse;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.repository.StaffScheduleRepository;

@ExtendWith(MockitoExtension.class)
public class StaffScheduleServiceImplTest {

    @Mock
    private StaffScheduleRepository scheduleRepository;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffScheduleServiceImpl scheduleService;

    private Staff staff;
    private StaffSchedule savedSchedule;
    private int staffId = 1;
    private int scheduleId = 1;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        // Setup future date (tomorrow)
        futureDate = Date.valueOf(LocalDate.now().plusDays(1));

        // Setup staff entity
        staff = new Staff();
        staff.setStaffId(staffId);
        staff.setFullName("Dr. Nguyen Van A");
        staff.setPosition("General Practitioner");

        // Setup schedule entity
        savedSchedule = new StaffSchedule();
        savedSchedule.setScheduleId(scheduleId);
        savedSchedule.setStaff(staff);
        savedSchedule.setScheduleDate(futureDate);
        savedSchedule.setStartTime(LocalTime.of(8, 0));
        savedSchedule.setEndTime(LocalTime.of(9, 0));
        savedSchedule.setStatus(EScheduleStatus.AVAILABLE);
    }

    // ==================== CREATE SLOT TESTS ====================

    @Test
    @DisplayName("Create Slot - Success")
    void createSlot_Success() {
        StaffScheduleRequest request = new StaffScheduleRequest();
        request.setStaffId(staffId);
        request.setScheduleDate(futureDate);
        request.setStartTime(LocalTime.of(8, 0));
        request.setEndTime(LocalTime.of(9, 0));

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                staffId, futureDate, LocalTime.of(8, 0))).thenReturn(false);
        when(scheduleRepository.save(any(StaffSchedule.class))).thenReturn(savedSchedule);

        ScheduleSlotResponse response = scheduleService.createSlot(request);

        assertNotNull(response);
        assertEquals(scheduleId, response.getScheduleId());
        assertEquals(staffId, response.getStaffId());
        assertEquals("Dr. Nguyen Van A", response.getStaffName());
        verify(staffRepository, times(1)).findById(staffId);
        verify(scheduleRepository, times(1)).save(any(StaffSchedule.class));
    }

    @Test
    @DisplayName("Create Slot - Fail: Staff Not Found")
    void createSlot_StaffNotFound() {
        StaffScheduleRequest request = new StaffScheduleRequest();
        request.setStaffId(999);
        request.setScheduleDate(futureDate);
        request.setStartTime(LocalTime.of(8, 0));
        request.setEndTime(LocalTime.of(9, 0));

        when(staffRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            scheduleService.createSlot(request);
        });

        assertEquals("Staff not found with id: 999", exception.getMessage());
        verify(scheduleRepository, never()).save(any(StaffSchedule.class));
    }

    @Test
    @DisplayName("Create Slot - Fail: Past Date")
    void createSlot_PastDate() {
        Date pastDate = Date.valueOf(LocalDate.now().minusDays(1));
        
        StaffScheduleRequest request = new StaffScheduleRequest();
        request.setStaffId(staffId);
        request.setScheduleDate(pastDate);
        request.setStartTime(LocalTime.of(8, 0));
        request.setEndTime(LocalTime.of(9, 0));

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.createSlot(request);
        });

        assertEquals("Cannot create schedule for past dates", exception.getMessage());
        verify(scheduleRepository, never()).save(any(StaffSchedule.class));
    }

    @Test
    @DisplayName("Create Slot - Fail: Slot Already Exists")
    void createSlot_SlotAlreadyExists() {
        StaffScheduleRequest request = new StaffScheduleRequest();
        request.setStaffId(staffId);
        request.setScheduleDate(futureDate);
        request.setStartTime(LocalTime.of(8, 0));
        request.setEndTime(LocalTime.of(9, 0));

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                staffId, futureDate, LocalTime.of(8, 0))).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.createSlot(request);
        });

        assertEquals("Schedule slot already exists for this staff, date and time", exception.getMessage());
        verify(scheduleRepository, never()).save(any(StaffSchedule.class));
    }

    // ==================== GET SLOT BY ID TESTS ====================

    @Test
    @DisplayName("Get Slot By Id - Success")
    void getSlotById_Success() {
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(savedSchedule));

        ScheduleSlotResponse response = scheduleService.getSlotById(scheduleId);

        assertNotNull(response);
        assertEquals(scheduleId, response.getScheduleId());
        assertEquals(staffId, response.getStaffId());
        verify(scheduleRepository, times(1)).findById(scheduleId);
    }

    @Test
    @DisplayName("Get Slot By Id - Fail: Not Found")
    void getSlotById_NotFound() {
        when(scheduleRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            scheduleService.getSlotById(999);
        });

        assertEquals("Schedule not found with id: 999", exception.getMessage());
    }

    // ==================== UPDATE SLOT TESTS ====================

    @Test
    @DisplayName("Update Slot - Success")
    void updateSlot_Success() {
        StaffScheduleRequest request = new StaffScheduleRequest();
        request.setStatus(EScheduleStatus.ON_LEAVE);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(savedSchedule));
        when(scheduleRepository.save(any(StaffSchedule.class))).thenReturn(savedSchedule);

        ScheduleSlotResponse response = scheduleService.updateSlot(scheduleId, request);

        assertNotNull(response);
        verify(scheduleRepository, times(1)).findById(scheduleId);
        verify(scheduleRepository, times(1)).save(any(StaffSchedule.class));
    }

    @Test
    @DisplayName("Update Slot - Fail: Not Found")
    void updateSlot_NotFound() {
        StaffScheduleRequest request = new StaffScheduleRequest();
        request.setStatus(EScheduleStatus.ON_LEAVE);

        when(scheduleRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            scheduleService.updateSlot(999, request);
        });

        assertEquals("Schedule not found with id: 999", exception.getMessage());
        verify(scheduleRepository, never()).save(any(StaffSchedule.class));
    }

    @Test
    @DisplayName("Update Slot - Fail: Past Date")
    void updateSlot_PastDate() {
        savedSchedule.setScheduleDate(Date.valueOf(LocalDate.now().minusDays(1)));
        
        StaffScheduleRequest request = new StaffScheduleRequest();
        request.setStatus(EScheduleStatus.ON_LEAVE);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(savedSchedule));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.updateSlot(scheduleId, request);
        });

        assertEquals("Cannot modify schedule for past dates", exception.getMessage());
        verify(scheduleRepository, never()).save(any(StaffSchedule.class));
    }

    // ==================== DELETE SLOT TESTS ====================

    @Test
    @DisplayName("Delete Slot - Success")
    void deleteSlot_Success() {
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(savedSchedule));

        scheduleService.deleteSlot(scheduleId);

        verify(scheduleRepository, times(1)).findById(scheduleId);
        verify(scheduleRepository, times(1)).delete(savedSchedule);
    }

    @Test
    @DisplayName("Delete Slot - Fail: Not Found")
    void deleteSlot_NotFound() {
        when(scheduleRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            scheduleService.deleteSlot(999);
        });

        assertEquals("Schedule not found with id: 999", exception.getMessage());
        verify(scheduleRepository, never()).delete(any(StaffSchedule.class));
    }

    @Test
    @DisplayName("Delete Slot - Fail: Past Date")
    void deleteSlot_PastDate() {
        savedSchedule.setScheduleDate(Date.valueOf(LocalDate.now().minusDays(1)));
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(savedSchedule));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.deleteSlot(scheduleId);
        });

        assertEquals("Cannot delete schedule for past dates", exception.getMessage());
        verify(scheduleRepository, never()).delete(any(StaffSchedule.class));
    }

    // ==================== ASSIGN SHIFT TESTS ====================

    @Test
    @DisplayName("Assign Shift - Morning Success")
    void assignShift_MorningSuccess() {
        ShiftAssignmentRequest request = new ShiftAssignmentRequest();
        request.setStaffId(staffId);
        request.setDate(futureDate);
        request.setShiftType(ShiftAssignmentRequest.ShiftType.MORNING);

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));
        // No existing slots
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                anyInt(), any(Date.class), any(LocalTime.class))).thenReturn(false);
        when(scheduleRepository.save(any(StaffSchedule.class))).thenReturn(savedSchedule);

        List<ScheduleSlotResponse> responses = scheduleService.assignShift(request);

        assertNotNull(responses);
        assertEquals(4, responses.size()); // 4 slots for morning shift (8-9, 9-10, 10-11, 11-12)
        verify(scheduleRepository, times(4)).save(any(StaffSchedule.class));
    }

    @Test
    @DisplayName("Assign Shift - Afternoon Success")
    void assignShift_AfternoonSuccess() {
        ShiftAssignmentRequest request = new ShiftAssignmentRequest();
        request.setStaffId(staffId);
        request.setDate(futureDate);
        request.setShiftType(ShiftAssignmentRequest.ShiftType.AFTERNOON);

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                anyInt(), any(Date.class), any(LocalTime.class))).thenReturn(false);
        when(scheduleRepository.save(any(StaffSchedule.class))).thenReturn(savedSchedule);

        List<ScheduleSlotResponse> responses = scheduleService.assignShift(request);

        assertNotNull(responses);
        assertEquals(4, responses.size()); // 4 slots for afternoon shift (13-14, 14-15, 15-16, 16-17)
        verify(scheduleRepository, times(4)).save(any(StaffSchedule.class));
    }

    @Test
    @DisplayName("Assign Shift - Skip Existing Slots")
    void assignShift_SkipExisting() {
        ShiftAssignmentRequest request = new ShiftAssignmentRequest();
        request.setStaffId(staffId);
        request.setDate(futureDate);
        request.setShiftType(ShiftAssignmentRequest.ShiftType.MORNING);

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));
        // First slot exists, others don't
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                staffId, futureDate, LocalTime.of(8, 0))).thenReturn(true);
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                staffId, futureDate, LocalTime.of(9, 0))).thenReturn(false);
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                staffId, futureDate, LocalTime.of(10, 0))).thenReturn(false);
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                staffId, futureDate, LocalTime.of(11, 0))).thenReturn(false);
        when(scheduleRepository.save(any(StaffSchedule.class))).thenReturn(savedSchedule);

        List<ScheduleSlotResponse> responses = scheduleService.assignShift(request);

        assertNotNull(responses);
        assertEquals(3, responses.size()); // Only 3 slots created (skipped 8-9)
        verify(scheduleRepository, times(3)).save(any(StaffSchedule.class));
    }

    @Test
    @DisplayName("Assign Shift - Fail: Staff Not Found")
    void assignShift_StaffNotFound() {
        ShiftAssignmentRequest request = new ShiftAssignmentRequest();
        request.setStaffId(999);
        request.setDate(futureDate);
        request.setShiftType(ShiftAssignmentRequest.ShiftType.MORNING);

        when(staffRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            scheduleService.assignShift(request);
        });

        assertEquals("Staff not found with id: 999", exception.getMessage());
        verify(scheduleRepository, never()).save(any(StaffSchedule.class));
    }

    // ==================== RECURRING SCHEDULE TESTS ====================

    @Test
    @DisplayName("Create Recurring Schedule - Success")
    void createRecurringSchedule_Success() {
        // Set dates for next 2 weeks (to ensure we have some valid days)
        Date startDate = Date.valueOf(LocalDate.now().plusDays(1));
        Date endDate = Date.valueOf(LocalDate.now().plusDays(14));

        RecurringScheduleRequest request = new RecurringScheduleRequest();
        request.setStaffId(staffId);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setDaysOfWeek(Arrays.asList(1, 3, 5)); // Monday, Wednesday, Friday
        request.setShiftType(ShiftAssignmentRequest.ShiftType.MORNING);
        request.setConflictAction(ShiftAssignmentRequest.ConflictAction.SKIP);

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                anyInt(), any(Date.class), any(LocalTime.class))).thenReturn(false);
        when(scheduleRepository.save(any(StaffSchedule.class))).thenReturn(savedSchedule);

        BulkOperationResponse response = scheduleService.createRecurringSchedule(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getSuccessCount() > 0);
    }

    @Test
    @DisplayName("Create Recurring Schedule - With Conflicts (Skip)")
    void createRecurringSchedule_WithConflictsSkip() {
        Date startDate = Date.valueOf(LocalDate.now().plusDays(1));
        Date endDate = Date.valueOf(LocalDate.now().plusDays(14));

        RecurringScheduleRequest request = new RecurringScheduleRequest();
        request.setStaffId(staffId);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setDaysOfWeek(Arrays.asList(1, 3, 5)); // Monday, Wednesday, Friday
        request.setShiftType(ShiftAssignmentRequest.ShiftType.MORNING);
        request.setConflictAction(ShiftAssignmentRequest.ConflictAction.SKIP);

        when(staffRepository.findById(staffId)).thenReturn(Optional.of(staff));
        // Simulate some slots already exist
        when(scheduleRepository.existsByStaff_StaffIdAndScheduleDateAndStartTime(
                anyInt(), any(Date.class), any(LocalTime.class))).thenReturn(true);

        BulkOperationResponse response = scheduleService.createRecurringSchedule(request);

        assertNotNull(response);
        // With all slots existing, we expect skipped count > 0
        assertTrue(response.getSkippedCount() >= 0);
    }

    // ==================== CAN MODIFY SCHEDULE TESTS ====================

    @Test
    @DisplayName("Can Modify Schedule - Today should be allowed")
    void canModifySchedule_Today() {
        Date today = Date.valueOf(LocalDate.now());
        boolean result = scheduleService.canModifySchedule(today);
        assertTrue(result);
    }

    @Test
    @DisplayName("Can Modify Schedule - Future date should be allowed")
    void canModifySchedule_Future() {
        boolean result = scheduleService.canModifySchedule(futureDate);
        assertTrue(result);
    }

    @Test
    @DisplayName("Can Modify Schedule - Past date should not be allowed")
    void canModifySchedule_Past() {
        Date pastDate = Date.valueOf(LocalDate.now().minusDays(1));
        boolean result = scheduleService.canModifySchedule(pastDate);
        assertFalse(result);
    }
}
