package com.example.ooad.service.staff.implementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EGender;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.domain.enums.EStatus;
import com.example.ooad.dto.request.CreateAccountDto;
import com.example.ooad.dto.request.StaffRequest;
import com.example.ooad.dto.response.AccountResponse;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.dto.request.StaffFilterRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.service.auth.interfaces.AuthService;
import com.example.ooad.service.email.interfaces.EmailService;
import com.example.ooad.validator.ActorValidator;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class StaffServiceImplementationTest {

    @Mock
    StaffRepository staffRepo;

    @Mock
    StaffScheduleRepository staffScheduleRepo;

    @Mock
    AccountRepository accountRepo;

    @Mock
    ActorValidator actorValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthService authService;

    @Mock
    EmailService emailService;

    @InjectMocks
    StaffServiceImplementation service;

    private StaffRequest buildRequest() {
        return new StaffRequest(
                "Nguyen Van A",
                Date.valueOf("1990-01-01"),
                EGender.MALE,
                "a.nguyen@example.com",
                "0123456789",
                "123456789",
                "General",
                true,
                ERole.DOCTOR);
    }

    @Test
    public void createStaff_happyPath_createsStaffAndAccount() {
        StaffRequest req = buildRequest();
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);
        when(actorValidator.validateEmail(req.getEmail())).thenReturn(true);
        when(actorValidator.validatePhoneNumber(req.getPhone())).thenReturn(true);
        when(actorValidator.validateIdCard(req.getIdCard())).thenReturn(true);

        when(staffRepo.findAll()).thenReturn(Collections.emptyList());

        Staff saved = new Staff();
        saved.setStaffId(10);
        saved.setFullName(req.getFullName());
        saved.setEmail(req.getEmail());
        when(staffRepo.save(any(Staff.class))).thenReturn(saved);

        var resp = service.createStaff(req, br);

        assertNotNull(resp);
        assertEquals(10, resp.getStaffId());
        verify(staffRepo, times(1)).save(any(Staff.class));
    }

    @Test
    public void updateStaff_happyPath_updatesStaffAndAccount() {
        int id = 10;
        Staff existing = new Staff();
        existing.setStaffId(id);
        Account acc = new Account(2, "old@example.com", "p", ERole.DOCTOR, EStatus.ACTIVE);
        existing.setAccount(acc);
        when(staffRepo.findById(id)).thenReturn(Optional.of(existing));

        StaffRequest req = buildRequest();
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);
        when(actorValidator.validateEmail(req.getEmail())).thenReturn(true);
        when(actorValidator.validatePhoneNumber(req.getPhone())).thenReturn(true);
        when(actorValidator.validateIdCard(req.getIdCard())).thenReturn(true);

        // staffRepo.findAll returns other staffs without conflict
        when(staffRepo.findAll()).thenReturn(Collections.emptyList());

        when(staffRepo.save(any(Staff.class))).thenAnswer(inv -> inv.getArgument(0));
        var resp = service.updateStaff(id, req, br);
        assertNotNull(resp);
        assertEquals(req.getFullName(), resp.getFullName());
        verify(staffRepo, times(1)).save(any(Staff.class));
    }

    @Test
    public void updateStaff_conflictEmail_throwsConflict() {
        int id = 11;
        Staff existing = new Staff();
        existing.setStaffId(id);
        when(staffRepo.findById(id)).thenReturn(Optional.of(existing));

        Staff other = new Staff();
        other.setStaffId(12);
        other.setEmail("dup@example.com");
        when(staffRepo.findAll()).thenReturn(List.of(other));

        StaffRequest req = buildRequest();
        req.setEmail("dup@example.com");
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);
        when(actorValidator.validateEmail(req.getEmail())).thenReturn(true);
        when(actorValidator.validatePhoneNumber(req.getPhone())).thenReturn(true);
        when(actorValidator.validateIdCard(req.getIdCard())).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.updateStaff(id, req, br));
    }

    @Test
    public void getStaffById_found_returnsStaffResponse() {
        int id = 20;
        Staff s = new Staff();
        s.setStaffId(id);
        s.setFullName("X Y");
        when(staffRepo.findById(id)).thenReturn(Optional.of(s));

        var resp = service.getStaffById(id);
        assertNotNull(resp);
        assertEquals(id, resp.getStaffId());
    }

    @Test
    public void getStaffById_notFound_throws() {
        when(staffRepo.findById(999)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getStaffById(999));
    }

    @Test
    public void deleteStaff_happyPath_deletesBoth() {
        int id = 30;
        Staff s = new Staff();
        s.setStaffId(id);
        Account acc = new Account(5, "del@example.com", "p", ERole.RECEPTIONIST, EStatus.ACTIVE);
        s.setAccount(acc);
        when(staffRepo.findById(id)).thenReturn(Optional.of(s));

        service.deleteStaff(id);
        verify(staffRepo, times(1)).delete(s);
        verify(accountRepo, times(1)).delete(acc);
    }

    @Test
    public void deleteStaff_notFound_throws() {
        when(staffRepo.findById(400)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deleteStaff(400));
    }

    @Test
    public void searchStaffs_filters_returnPage() {
        StaffFilterRequest filter = new StaffFilterRequest();
        filter.setPage(0);
        filter.setSize(10);
        filter.setSearch("nguyen");
        filter.setRole(ERole.DOCTOR);
        filter.setActive(true);

        Staff s = new Staff();
        s.setStaffId(50);
        s.setFullName("Nguyen A");
        Account acc = new Account(7, "n@example.com", "p", ERole.DOCTOR, EStatus.ACTIVE);
        s.setAccount(acc);
        Page<Staff> page = new PageImpl<>(List.of(s));
        when(staffRepo.findAll((Specification<Staff>) any(), (Pageable) any())).thenReturn(page);

        var result = service.searchStaffs(filter);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void createStaff_duplicateEmail_throwsConflict() {
        StaffRequest req = buildRequest();
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(false);
        when(actorValidator.validateEmail(req.getEmail())).thenReturn(true);
        when(actorValidator.validatePhoneNumber(req.getPhone())).thenReturn(true);
        when(actorValidator.validateIdCard(req.getIdCard())).thenReturn(true);

        Staff existing = new Staff();
        existing.setEmail(req.getEmail());
        when(staffRepo.findAll()).thenReturn(List.of(existing));

        assertThrows(ConflictException.class, () -> service.createStaff(req, br));
    }

    @Test
    public void isStaffFree_overlappingSchedule_returnsFalse() {
        int staffId = 5;
        Date date = Date.valueOf("2025-12-16");
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(10, 0);

        StaffSchedule s = new StaffSchedule();
        s.setStartTime(LocalTime.of(9, 30));
        s.setEndTime(LocalTime.of(10, 30));
        when(staffScheduleRepo.findByStaff_StaffIdAndScheduleDateAndStatusNot(eq(staffId), eq(date), any()))
                .thenReturn(List.of(s));

        boolean free = service.isStaffFree(date, start, end, staffId);
        assertFalse(free);
    }

    @Test
    public void isStaffFree_nonOverlappingSchedule_returnsTrue() {
        int staffId = 5;
        Date date = Date.valueOf("2025-12-16");
        LocalTime start = LocalTime.of(7, 0);
        LocalTime end = LocalTime.of(8, 0);

        StaffSchedule s = new StaffSchedule();
        s.setStartTime(LocalTime.of(9, 0));
        s.setEndTime(LocalTime.of(10, 0));
        when(staffScheduleRepo.findByStaff_StaffIdAndScheduleDateAndStatusNot(eq(staffId), eq(date), any()))
                .thenReturn(List.of(s));

        boolean free = service.isStaffFree(date, start, end, staffId);
        assertTrue(free);
    }

    @Test
    public void createStaff_bindingErrors_throwsBadRequest() {
        StaffRequest req = buildRequest();
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(true);
        org.springframework.validation.ObjectError err = new org.springframework.validation.ObjectError("obj", "bad");
        when(br.getAllErrors()).thenReturn(List.of(err));

        assertThrows(BadRequestException.class, () -> service.createStaff(req, br));
    }

    @Test
    public void updateStaff_bindingErrors_throwsBadRequest() {
        int id = 5;
        Staff s = new Staff();
        s.setStaffId(id);

        StaffRequest req = buildRequest();
        BindingResult br = mock(BindingResult.class);
        when(br.hasErrors()).thenReturn(true);
        org.springframework.validation.ObjectError err = new org.springframework.validation.ObjectError("obj", "bad");
        when(br.getAllErrors()).thenReturn(List.of(err));

        assertThrows(BadRequestException.class, () -> service.updateStaff(id, req, br));
    }
}
