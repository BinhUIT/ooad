package com.example.ooad.service.appointment.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.PageRanges;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.example.ooad.domain.entity.Appointment;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.StaffSchedule;
import com.example.ooad.domain.enums.EAppointmentStatus;
import com.example.ooad.domain.enums.EScheduleStatus;
import com.example.ooad.dto.request.AppointmentRequest;
import com.example.ooad.dto.request.BookAppointmentRequest;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.exception.UnauthorizedException;
import com.example.ooad.repository.AppointmentRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.StaffScheduleRepository;
import com.example.ooad.service.patient.interfaces.PatientService;
import com.example.ooad.service.staff.interfaces.StaffService;
import com.example.ooad.utils.Message;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    private StaffScheduleRepository staffScheduleRepo;

    @Mock
    private PatientRepository patientRepo;

    @Mock
    private  PatientService patientService;

    @Mock
    private StaffService staffService;

    @Mock
    private AppointmentRepository appointmentRepo;

    @Spy
    @InjectMocks
    private AppointmentServiceImplementation appointmentService;

    @Test
    void findAppoitmentByIdTestSuccess() {
        Appointment appointment = new Appointment();

        when(appointmentRepo.findById(anyInt())).thenReturn(Optional.of(appointment));
        
        Appointment result = appointmentService.findAppointmentById(1);

        assertNotNull(result);
    }

    @Test
    void findAppointmentByIdFail() {
        when(appointmentRepo.findById(anyInt())).thenReturn(Optional.empty());

         NotFoundException exception = assertThrows(NotFoundException.class, ()->{
            appointmentService.findAppointmentById(1);
        });

        assertNotNull(exception);
        assertEquals(Message.appointmentNotFound, exception.getMessage());
    }

    @Test
    void testBookAppointmentSuccess() {
        StaffSchedule fakeSchedule = new StaffSchedule();
        fakeSchedule.setStatus(EScheduleStatus.AVAILABLE);

        

        when(staffScheduleRepo.findById(anyInt())).thenReturn(Optional.of(fakeSchedule));
        doReturn(true).when(appointmentService).checkSchedule(any(StaffSchedule.class));
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(new Appointment());

        Appointment result = appointmentService.bookAppointment(new BookAppointmentRequest(1), new Patient());

        assertNotNull(result);
        verify(appointmentRepo,times(1)).save(any(Appointment.class));

    }

    @Test
    void testBookAppointmentFailWrongStatus() {
        StaffSchedule fakeSchedule = new StaffSchedule();
        fakeSchedule.setStatus(EScheduleStatus.BOOKED);

        when(staffScheduleRepo.findById(anyInt())).thenReturn(Optional.of(fakeSchedule));
        doReturn(true).when(appointmentService).checkSchedule(any(StaffSchedule.class));

        BadRequestException exception = assertThrows(BadRequestException.class, ()->{
            appointmentService.bookAppointment(new BookAppointmentRequest(1), new Patient());
        });

        assertNotNull(exception);
        assertEquals(Message.cannotBookAppointment, exception.getMessage());
        verify(appointmentRepo,never()).save(any(Appointment.class));
    }

    @Test
    void testBookAppointmentFailInvalidTime() {
        StaffSchedule fakeSchedule = new StaffSchedule();
        fakeSchedule.setStatus(EScheduleStatus.AVAILABLE);

        when(staffScheduleRepo.findById(anyInt())).thenReturn(Optional.of(fakeSchedule));
        doReturn(false).when(appointmentService).checkSchedule(any(StaffSchedule.class));

        BadRequestException exception = assertThrows(BadRequestException.class, ()->{
            appointmentService.bookAppointment(new BookAppointmentRequest(1), new Patient());
        });

        assertNotNull(exception);
        assertEquals(Message.cannotBookAppointment, exception.getMessage());
        verify(appointmentRepo,never()).save(any(Appointment.class));
    }

    @Test
void getAppointmentHistory_BothStatusAndDatePresent_ShouldCallCorrectRepoMethod() {
    Optional<EAppointmentStatus> status = Optional.of(EAppointmentStatus.COMPLETED);
    Optional<Date> date = Optional.of(new Date(System.currentTimeMillis()));
    Patient patient = new Patient();
    patient.setPatientId(1);
    PageRequest   pageable = PageRequest.of(0, 10);
    Page<Appointment>  mockPage = new PageImpl<>(Collections.emptyList());

    when(appointmentRepo.findByPatient_PatientIdAndAppointmentDateAndStatus(pageable, patient.getPatientId(), status.get(), date.get()))
            .thenReturn(mockPage);

    Page<Appointment> result = appointmentService.getAppointmentHistory(0, 10, patient, status, date);

    assertNotNull(result);
    verify(appointmentRepo, times(1)).findByPatient_PatientIdAndAppointmentDateAndStatus(any(), anyInt(), any(), any());
}

@Test
void getAppointmentHistory_OnlyStatus_ShouldCallCorrectRepoMethod() {
    Optional<EAppointmentStatus> status = Optional.of(EAppointmentStatus.COMPLETED);
    Optional<Date> date = Optional.empty();
    Patient patient = new Patient();
    patient.setPatientId(1);
    PageRequest   pageable = PageRequest.of(0, 10);
    Page<Appointment>  mockPage = new PageImpl<>(Collections.emptyList());

    when(appointmentRepo.findByPatient_PatientIdAndStatus(pageable,patient.getPatientId(),status.get())).thenReturn(mockPage);

    Page<Appointment> result = appointmentService.getAppointmentHistory(0, 10, patient, status, date);

    assertNotNull(result);
    verify(appointmentRepo, times(1)).findByPatient_PatientIdAndStatus(any(), anyInt(), any());

}

@Test
void getAppointmentHistory_OnlyDate_ShouldCallCorrectRepoMethod() {
    Optional<EAppointmentStatus> status = Optional.empty();
    Optional<Date> date = Optional.of(new Date(System.currentTimeMillis()));
    Patient patient = new Patient();
    patient.setPatientId(1);
    PageRequest   pageable = PageRequest.of(0, 10);
    Page<Appointment>  mockPage = new PageImpl<>(Collections.emptyList());

    when(appointmentRepo.findByPatient_PatientIdAndAppointmentDate(pageable, patient.getPatientId(), date.get())).thenReturn(mockPage);
    Page<Appointment> result = appointmentService.getAppointmentHistory(0, 10, patient, status, date);
    assertNotNull(result);
    verify(appointmentRepo, times(1)).findByPatient_PatientIdAndAppointmentDate(any(), anyInt(), any());
}

@Test
void getAppointmentHistoryNoneDateStatus_ShouldCallCorrectRepoMethod() {
    Optional<EAppointmentStatus> status = Optional.empty();
    Optional<Date> date = Optional.empty();
    Patient patient = new Patient();
    patient.setPatientId(1);
    PageRequest   pageable = PageRequest.of(0, 10);
    Page<Appointment>  mockPage = new PageImpl<>(Collections.emptyList());

    when(appointmentRepo.findByPatient_PatientId(pageable,patient.getPatientId())).thenReturn(mockPage);
    Page<Appointment> result = appointmentService.getAppointmentHistory(0, 10, patient, status, date);
    assertNotNull(result);
    verify(appointmentRepo, times(1)).findByPatient_PatientId(any(), anyInt());

}

@Test
void patientGetAppointmentByIdSuccess() {
    Patient fakePatient = new Patient();
    fakePatient.setPatientId(1);
    Appointment fakeAppointment = new Appointment();
    fakeAppointment.setPatient(fakePatient);

    when(appointmentRepo.findById(anyInt())).thenReturn(Optional.of(fakeAppointment));

    Appointment result = appointmentService.patientGetAppointmentById(fakePatient, 1);

    assertNotNull(result);
}

@Test
void patientGetAppointmentByIdNotFound() {
    Patient fakePatient = new Patient();
    when(appointmentRepo.findById(anyInt())).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, ()->{
        appointmentService.patientGetAppointmentById(fakePatient, 1);
    });
    assertNotNull(exception);
    assertEquals(Message.appointmentNotFound, exception.getMessage());
}

@Test
void patientGetAppointmentByIdNoPermission() {
    Patient fakePatient = new Patient();
    fakePatient.setPatientId(1);
    Appointment fakeAppointment = new Appointment();
    fakeAppointment.setPatient(new Patient());
    
    when(appointmentRepo.findById(anyInt())).thenReturn(Optional.of(fakeAppointment));

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, ()->{
        appointmentService.patientGetAppointmentById(fakePatient, 1);
    });
    assertNotNull(exception);
    assertEquals(Message.noPermission, exception.getMessage());
}
@Test
    void getScheduleOfDoctor_ShouldReturnList_WhenCallRepo() {
    
        int doctorId = 1;
        Date selectedDate = new Date(System.currentTimeMillis());
        
        StaffSchedule schedule1 = new StaffSchedule();
        schedule1.setStartTime(LocalTime.of(8, 0));
        StaffSchedule schedule2 = new StaffSchedule();
        schedule2.setStartTime(LocalTime.of(10, 0));
        
        List<StaffSchedule> mockSchedules = Arrays.asList(schedule1, schedule2);

        when(staffScheduleRepo.findByStaff_StaffIdAndScheduleDateOrderByStartTimeAsc(doctorId, selectedDate))
            .thenReturn(mockSchedules);

        
        List<StaffSchedule> result = appointmentService.getScheduleOfDoctor(doctorId, selectedDate);

        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(LocalTime.of(8, 0), result.get(0).getStartTime());

       
        verify(staffScheduleRepo, times(1))
            .findByStaff_StaffIdAndScheduleDateOrderByStartTimeAsc(doctorId, selectedDate);
    }

    @Test
    void getAppointments_AllParamsPresent_ShouldPassValuesToRepo() {
        
        Optional<String> name = Optional.of("John Doe");
        Optional<EAppointmentStatus> status = Optional.of(EAppointmentStatus.SCHEDULED);
        Date date =Date.valueOf(LocalDate.now());
        Optional<Date> appointmentDate = Optional.of(date);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Appointment> mockPage = new PageImpl<>(Collections.emptyList());

        when(appointmentRepo.getAppointments(pageable, status.get(), date, name.get()))
            .thenReturn(mockPage);

        
        Page<Appointment> result = appointmentService.getAppointmens(0, 10, name, status, appointmentDate);

       
        assertNotNull(result);
        verify(appointmentRepo).getAppointments(pageable, status.get(), date, name.get());
    }

    @Test
    void getAppointments_AllParamsEmpty_ShouldPassValuesToRepo() {
        Optional<String> name = Optional.empty();
        Optional<EAppointmentStatus> status = Optional.empty();
        
        Optional<Date> appointmentDate = Optional.empty();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Appointment> mockPage = new PageImpl<>(Collections.emptyList());

        when(appointmentRepo.getAppointments(pageable,null, null, null))
            .thenReturn(mockPage);

        
        Page<Appointment> result = appointmentService.getAppointmens(0, 10, name, status, appointmentDate);

       
        assertNotNull(result);
        verify(appointmentRepo).getAppointments(pageable, null, null, null);
    }

    @Test
    void endSession_ShouldUpdateStatusToNoShow_WhenAppointmentIsExpired() {
        
        Appointment expiredApp = new Appointment();
        expiredApp.setStatus(EAppointmentStatus.SCHEDULED);

        Appointment validApp = new Appointment();
        validApp.setStatus(EAppointmentStatus.SCHEDULED);

        List<Appointment> appointments = Arrays.asList(expiredApp, validApp);

        
        when(appointmentRepo.findByStatus(EAppointmentStatus.SCHEDULED)).thenReturn(appointments);

        
        doReturn(true).when(appointmentService).isAppointmentExpire(expiredApp);
        doReturn(false).when(appointmentService).isAppointmentExpire(validApp);

        
        appointmentService.endSession();

        
        assertEquals(EAppointmentStatus.NOSHOW, expiredApp.getStatus());
        
        assertEquals(EAppointmentStatus.SCHEDULED, validApp.getStatus());

        
        verify(appointmentRepo, times(1)).saveAll(appointments);
    }

    @Test
    void testReceptionistBookAppointmentSuccess() {
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(new Patient()));
        doReturn(new Appointment()).when(appointmentService).bookAppointment(any(), any());
        

        Appointment appointment = appointmentService.receptionistBookAppointment(new AppointmentRequest(1,1));

        assertNotNull(appointment);
        

    }

    @Test
    void testReceptioniseBookAppointmentFail() {
        when(patientRepo.findById(anyInt())).thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, ()->{
            appointmentService.receptionistBookAppointment(new AppointmentRequest(1,1));
        });
        assertNotNull(notFoundException);
        assertEquals(Message.patientNotFound, notFoundException.getMessage());
        verify(appointmentService, never()).bookAppointment(any(), any());
    }
    @Test
    void changeStatusToCancelled_ShouldCallFreeScheduleAndSave() {
        
        Appointment mockAppointment = new Appointment();
        mockAppointment.setAppointmentId(123);
        mockAppointment.setStatus(EAppointmentStatus.SCHEDULED);
        
        int appointmentId = 123;
        EAppointmentStatus newStatus = EAppointmentStatus.CANCELLED;

        
        doReturn(mockAppointment).when(appointmentService).findAppointmentById(appointmentId);
        
        
        doNothing().when(appointmentService).checkAuthority(any(), any());
        
        
        doNothing().when(appointmentService).freeSchedule(mockAppointment);
        
        
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(mockAppointment);

        
        Appointment result = appointmentService.changeAppointmentStatus(null, appointmentId, newStatus);

        
        assertNotNull(result);
        assertEquals(EAppointmentStatus.CANCELLED, result.getStatus());
        
        
        verify(appointmentService, times(1)).freeSchedule(mockAppointment);
        verify(appointmentRepo, times(1)).save(mockAppointment);
    }

    @Test
    void changeStatusToCompleted_ShouldNotCallFreeSchedule() {

        Appointment mockAppointment = new Appointment();
        mockAppointment.setAppointmentId(123);
        mockAppointment.setStatus(EAppointmentStatus.SCHEDULED);
        
        int appointmentId = 123;
        EAppointmentStatus newStatus = EAppointmentStatus.COMPLETED;

        doReturn(mockAppointment).when(appointmentService).findAppointmentById(appointmentId);
        doNothing().when(appointmentService).checkAuthority(any(), any());
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(mockAppointment);

        
        Appointment result = appointmentService.changeAppointmentStatus(null, appointmentId, newStatus);

        
        assertEquals(EAppointmentStatus.COMPLETED, result.getStatus());
        
        
        verify(appointmentService, never()).freeSchedule(any());
        verify(appointmentRepo, times(1)).save(mockAppointment);
    }

    @Test
    void editAppointment_Success() {
        
        Appointment mockAppointment = new Appointment();
        mockAppointment.setStatus(EAppointmentStatus.SCHEDULED);

        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1);
        request.setScheduleId(100);

        Patient mockPatient = new Patient();
        mockPatient.setPatientId(1);

        StaffSchedule mockSchedule = new StaffSchedule();
        mockSchedule.setScheduleId(100);
        mockSchedule.setStaff(new Staff());

        doReturn(mockAppointment).when(appointmentService).findAppointmentById(anyInt());
        doNothing().when(appointmentService).checkAuthority(any(), any());
        doReturn(true).when(appointmentService).checkSchedule(any());
        doNothing().when(appointmentService).freeSchedule(any());

        // Stub Repo
        when(patientRepo.findById(1)).thenReturn(Optional.of(mockPatient));
        when(staffScheduleRepo.findById(100)).thenReturn(Optional.of(mockSchedule));
        when(appointmentRepo.save(any())).thenReturn(mockAppointment);

        // 2. Act
        Appointment result = appointmentService.editAppointment(null, 1, request);

        // 3. Assert & Verify
        assertNotNull(result);
        assertEquals(EScheduleStatus.BOOKED, mockSchedule.getStatus()); // Kiểm tra schedule bị đổi status
        verify(staffScheduleRepo).save(mockSchedule);
        verify(appointmentRepo).save(mockAppointment);
        verify(appointmentService).freeSchedule(any()); // Đảm bảo đã giải phóng lịch cũ
    }

    @Test
    void editAppointment_ThrowBadRequest_WhenStatusNotScheduled() {

        Appointment mockAppointment = new Appointment();
        mockAppointment.setStatus(EAppointmentStatus.SCHEDULED);

        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1);
        request.setScheduleId(100);

        Patient mockPatient = new Patient();
        mockPatient.setPatientId(1);

        StaffSchedule mockSchedule = new StaffSchedule();
        mockSchedule.setScheduleId(100);
        mockSchedule.setStaff(new Staff());
        // 1. Arrange - Giả lập trạng thái không phải SCHEDULED
        mockAppointment.setStatus(EAppointmentStatus.COMPLETED);
        doReturn(mockAppointment).when(appointmentService).findAppointmentById(anyInt());

        // 2. Act & Assert
        BadRequestException ex = assertThrows(BadRequestException.class, () -> 
            appointmentService.editAppointment(null, 1, request)
        );
        assertEquals(Message.cannotEdit, ex.getMessage());
        verify(appointmentRepo, never()).save(any());
    }

    @Test
    void editAppointment_ThrowNotFound_WhenPatientNotExists() {

        Appointment mockAppointment = new Appointment();
        mockAppointment.setStatus(EAppointmentStatus.SCHEDULED);

        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1);
        request.setScheduleId(100);

        Patient mockPatient = new Patient();
        mockPatient.setPatientId(1);

        StaffSchedule mockSchedule = new StaffSchedule();
        mockSchedule.setScheduleId(100);
        mockSchedule.setStaff(new Staff());
        // 1. Arrange
        doReturn(mockAppointment).when(appointmentService).findAppointmentById(anyInt());
        doNothing().when(appointmentService).checkAuthority(any(), any());
        when(patientRepo.findById(1)).thenReturn(Optional.empty()); // Patient không tồn tại

        // 2. Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> 
            appointmentService.editAppointment(null, 1, request)
        );
        assertEquals(Message.patientNotFound, ex.getMessage());
    }

    @Test
    void editAppointment_ThrowBadRequest_WhenScheduleInvalid() {
        Appointment mockAppointment = new Appointment();
        mockAppointment.setStatus(EAppointmentStatus.SCHEDULED);

        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(1);
        request.setScheduleId(100);

        Patient mockPatient = new Patient();
        mockPatient.setPatientId(1);

        StaffSchedule mockSchedule = new StaffSchedule();
        mockSchedule.setScheduleId(100);
        mockSchedule.setStaff(new Staff());
        // 1. Arrange
        doReturn(mockAppointment).when(appointmentService).findAppointmentById(anyInt());
        doNothing().when(appointmentService).checkAuthority(any(), any());
        
        when(patientRepo.findById(1)).thenReturn(Optional.of(mockPatient));
        when(staffScheduleRepo.findById(100)).thenReturn(Optional.of(mockSchedule));
        
        // Giả lập checkSchedule trả về false
        doReturn(false).when(appointmentService).checkSchedule(mockSchedule);

        // 2. Act & Assert
        BadRequestException ex = assertThrows(BadRequestException.class, () -> 
            appointmentService.editAppointment(null, 1, request)
        );
        assertEquals(Message.cannotBookAppointment, ex.getMessage());
    }

    @Test
    void getScheduleId_Success_ShouldReturnId() {

        Date testDate = Date.valueOf("2026-01-05");
        LocalTime testTime = LocalTime.of(10, 0);

        Appointment mockAppointment = new Appointment();
        mockAppointment.setAppointmentId(1);
        Staff staff = new Staff();
        staff.setPosition("Doctor");
        staff.setStaffId(10);
        mockAppointment.setStaff(staff); 
        mockAppointment.setAppointmentDate(testDate);
        mockAppointment.setAppointmentTime(testTime);
        // 1. Arrange
        StaffSchedule mockSchedule = new StaffSchedule();
        mockSchedule.setScheduleId(999);

        // Stub hàm nội bộ bằng Spy
        doReturn(mockAppointment).when(appointmentService).findAppointmentById(1);

        // Stub Repository
        when(staffScheduleRepo.findByStaff_StaffIdAndStartTimeAndScheduleDate(
                10, testTime, testDate)).thenReturn(mockSchedule);

        // 2. Act
        Integer result = appointmentService.getScheduleId(1);

        // 3. Assert
        assertEquals(999, result);
        verify(staffScheduleRepo).findByStaff_StaffIdAndStartTimeAndScheduleDate(10, testTime, testDate);
    }

    @Test
    void getScheduleId_NotFound_ShouldReturnZero() {
        Date testDate = Date.valueOf("2026-01-05");
        LocalTime testTime = LocalTime.of(10, 0);

        Appointment mockAppointment = new Appointment();
        mockAppointment.setAppointmentId(1);
        Staff staff = new Staff();
        staff.setPosition("Doctor");
        staff.setStaffId(10);
        mockAppointment.setStaff(staff); 
        mockAppointment.setAppointmentDate(testDate);
        mockAppointment.setAppointmentTime(testTime);
        // 1. Arrange
        doReturn(mockAppointment).when(appointmentService).findAppointmentById(1);

        // Giả lập Repository không tìm thấy kết quả (null)
        when(staffScheduleRepo.findByStaff_StaffIdAndStartTimeAndScheduleDate(
                10, testTime, testDate)).thenReturn(null);

        // 2. Act
        Integer result = appointmentService.getScheduleId(1);

        // 3. Assert
        assertEquals(0, result);
    }

    @Test
    void getAppointmentOfDoctor_AllParamsPresent_ShouldCallRepoWithValues() {

        Staff mockStaff = new Staff();
        mockStaff.setStaffId(10);
        
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Appointment> mockPage = new PageImpl<>(Collections.emptyList());
        Authentication auth = new Authentication() {

            @Override
            public String getName() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getName'");
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
            }

            @Override
            public Object getCredentials() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getCredentials'");
            }

            @Override
            public Object getDetails() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getDetails'");
            }

            @Override
            public Object getPrincipal() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getPrincipal'");
            }

            @Override
            public boolean isAuthenticated() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'isAuthenticated'");
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'setAuthenticated'");
            }
            
        };
        // 1. Arrange
        Optional<String> patientName = Optional.of("Alice");
        Optional<EAppointmentStatus> status = Optional.of(EAppointmentStatus.SCHEDULED);
        Date date = Date.valueOf("2026-01-05");
        Optional<Date> appointmentDate = Optional.of(date);

        // Stub StaffService để trả về bác sĩ hiện tại
        when(staffService.getStaffFromAuth(any())).thenReturn(mockStaff);
        
        // Stub Repository
        when(appointmentRepo.getAppointmentsOfDoctor(pageable, 10, status.get(), date, "Alice"))
            .thenReturn(mockPage);

        // 2. Act
        Page<Appointment> result = appointmentService.getAppointmentOfDoctor(auth, 0, 10, patientName, status, appointmentDate);

        // 3. Assert
        assertNotNull(result);
        verify(staffService).getStaffFromAuth(auth);
        verify(appointmentRepo).getAppointmentsOfDoctor(pageable, 10, status.get(), date, "Alice");
    }

    @Test
    void getAppointmentOfDoctor_EmptyOptionals_ShouldCallRepoWithNulls() {

        Staff mockStaff = new Staff();
        mockStaff.setStaffId(10);
        
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Appointment> mockPage = new PageImpl<>(Collections.emptyList());
        Authentication auth = new Authentication() {

            @Override
            public String getName() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getName'");
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
            }

            @Override
            public Object getCredentials() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getCredentials'");
            }

            @Override
            public Object getDetails() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getDetails'");
            }

            @Override
            public Object getPrincipal() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getPrincipal'");
            }

            @Override
            public boolean isAuthenticated() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'isAuthenticated'");
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'setAuthenticated'");
            }
            
        };
        // 1. Arrange
        Optional<String> patientName = Optional.empty();
        Optional<EAppointmentStatus> status = Optional.empty();
        Optional<Date> appointmentDate = Optional.empty();

        when(staffService.getStaffFromAuth(auth)).thenReturn(mockStaff);
        
        // Kiểm tra xem orElse(null) có hoạt động đúng không
        when(appointmentRepo.getAppointmentsOfDoctor(pageable, 10, null, null, null))
            .thenReturn(mockPage);

        // 2. Act
        Page<Appointment> result = appointmentService.getAppointmentOfDoctor(auth, 0, 10, patientName, status, appointmentDate);

        // 3. Assert
        assertNotNull(result);
        verify(appointmentRepo).getAppointmentsOfDoctor(pageable, 10, null, null, null);
    }


   

    




}
