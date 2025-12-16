package com.example.ooad.service.profile.implementation;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EGender;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.dto.request.UpdateProfileRequest;
import com.example.ooad.dto.response.ProfileResponse;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.StaffRepository;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplementationTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private ProfileServiceImplementation profileService;

    private Account patientAccount;
    private Account staffAccount;
    private Patient patient;
    private Staff staff;
    private String patientUsername = "patient_user";
    private String staffUsername = "staff_user";

    @BeforeEach
    void setUp() {
        // Setup patient account
        patientAccount = new Account();
        patientAccount.setAccountId(1);
        patientAccount.setUsername(patientUsername);
        patientAccount.setRole(ERole.PATIENT);

        // Setup patient entity
        patient = new Patient();
        patient.setPatientId(1);
        patient.setFullName("Nguyen Van A");
        patient.setDateOfBirth(Date.valueOf(LocalDate.of(1990, 5, 15)));
        patient.setGender(EGender.MALE);
        patient.setPhone("0901234567");
        patient.setEmail("patient@example.com");
        patient.setIdCard("123456789012");
        patient.setAddress("123 Main St, HCM");
        patient.setFirstVisitDate(Date.valueOf(LocalDate.of(2024, 1, 1)));

        // Setup staff account
        staffAccount = new Account();
        staffAccount.setAccountId(2);
        staffAccount.setUsername(staffUsername);
        staffAccount.setRole(ERole.DOCTOR);

        // Setup staff entity
        staff = new Staff();
        staff.setStaffId(1);
        staff.setFullName("Tran Thi B");
        staff.setDateOfBirth(Date.valueOf(LocalDate.of(1985, 3, 20)));
        staff.setGender(EGender.FEMALE);
        staff.setPhone("0912345678");
        staff.setEmail("doctor@example.com");
        staff.setIdCard("987654321098");
        staff.setPosition("General Practitioner");
    }

    // ==================== GET MY PROFILE TESTS ====================

    @Test
    @DisplayName("Get My Profile - Success: Patient Role")
    void getMyProfile_PatientSuccess() {
        when(accountRepository.findByUsername(patientUsername)).thenReturn(patientAccount);
        when(patientRepository.findByAccountId(patientAccount.getAccountId())).thenReturn(Optional.of(patient));

        ProfileResponse response = profileService.getMyProfile(patientUsername);

        assertNotNull(response);
        assertEquals(patientUsername, response.getUsername());
        assertEquals(ERole.PATIENT, response.getRole());
        assertEquals("Nguyen Van A", response.getFullName());
        assertEquals(EGender.MALE, response.getGender());
        assertEquals("0901234567", response.getPhone());
        assertEquals("patient@example.com", response.getEmail());
        assertEquals("123 Main St, HCM", response.getAddress());
        assertNotNull(response.getFirstVisitDate());
        assertNull(response.getPosition()); // Position is for staff only

        verify(accountRepository, times(1)).findByUsername(patientUsername);
        verify(patientRepository, times(1)).findByAccountId(patientAccount.getAccountId());
        verify(staffRepository, never()).findByAccountId(anyInt());
    }

    @Test
    @DisplayName("Get My Profile - Success: Staff Role (Doctor)")
    void getMyProfile_StaffSuccess() {
        when(accountRepository.findByUsername(staffUsername)).thenReturn(staffAccount);
        when(staffRepository.findByAccountId(staffAccount.getAccountId())).thenReturn(Optional.of(staff));

        ProfileResponse response = profileService.getMyProfile(staffUsername);

        assertNotNull(response);
        assertEquals(staffUsername, response.getUsername());
        assertEquals(ERole.DOCTOR, response.getRole());
        assertEquals("Tran Thi B", response.getFullName());
        assertEquals(EGender.FEMALE, response.getGender());
        assertEquals("0912345678", response.getPhone());
        assertEquals("doctor@example.com", response.getEmail());
        assertEquals("General Practitioner", response.getPosition());
        assertNull(response.getAddress()); // Address is for patient only

        verify(accountRepository, times(1)).findByUsername(staffUsername);
        verify(staffRepository, times(1)).findByAccountId(staffAccount.getAccountId());
        verify(patientRepository, never()).findByAccountId(anyInt());
    }

    @Test
    @DisplayName("Get My Profile - Fail: Account Not Found")
    void getMyProfile_AccountNotFound() {
        when(accountRepository.findByUsername("unknown_user")).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            profileService.getMyProfile("unknown_user");
        });

        assertEquals("Không tìm thấy tài khoản", exception.getMessage());
        verify(patientRepository, never()).findByAccountId(anyInt());
        verify(staffRepository, never()).findByAccountId(anyInt());
    }

    @Test
    @DisplayName("Get My Profile - Patient account but no patient record")
    void getMyProfile_PatientNoRecord() {
        when(accountRepository.findByUsername(patientUsername)).thenReturn(patientAccount);
        when(patientRepository.findByAccountId(patientAccount.getAccountId())).thenReturn(Optional.empty());

        ProfileResponse response = profileService.getMyProfile(patientUsername);

        assertNotNull(response);
        assertEquals(patientUsername, response.getUsername());
        assertEquals(ERole.PATIENT, response.getRole());
        // Personal fields should be null since patient record is not found
        assertNull(response.getFullName());
        assertNull(response.getEmail());
    }

    // ==================== UPDATE MY PROFILE TESTS ====================

    @Test
    @DisplayName("Update My Profile - Success: Patient Update")
    void updateMyProfile_PatientSuccess() {
        when(accountRepository.findByUsername(patientUsername)).thenReturn(patientAccount);
        when(patientRepository.findByAccountId(patientAccount.getAccountId())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Nguyen Van A Updated");
        request.setPhone("0909999999");
        request.setGender(EGender.MALE);

        ProfileResponse response = profileService.updateMyProfile(patientUsername, request);

        assertNotNull(response);
        // updateMyProfile calls getMyProfile at the end, so findByAccountId is called twice
        verify(patientRepository, atLeast(1)).findByAccountId(patientAccount.getAccountId());
        verify(patientRepository, times(1)).save(any(Patient.class));
        verify(staffRepository, never()).save(any(Staff.class));
    }

    @Test
    @DisplayName("Update My Profile - Success: Staff Update")
    void updateMyProfile_StaffSuccess() {
        when(accountRepository.findByUsername(staffUsername)).thenReturn(staffAccount);
        when(staffRepository.findByAccountId(staffAccount.getAccountId())).thenReturn(Optional.of(staff));
        when(staffRepository.save(any(Staff.class))).thenReturn(staff);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Tran Thi B Updated");
        request.setEmail("doctor_updated@example.com");
        request.setGender(EGender.FEMALE);

        ProfileResponse response = profileService.updateMyProfile(staffUsername, request);

        assertNotNull(response);
        // updateMyProfile calls getMyProfile at the end, so findByAccountId is called twice
        verify(staffRepository, atLeast(1)).findByAccountId(staffAccount.getAccountId());
        verify(staffRepository, times(1)).save(any(Staff.class));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Update My Profile - Fail: Account Not Found")
    void updateMyProfile_AccountNotFound() {
        when(accountRepository.findByUsername("unknown_user")).thenReturn(null);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Test");

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            profileService.updateMyProfile("unknown_user", request);
        });

        assertEquals("Không tìm thấy tài khoản", exception.getMessage());
    }

    @Test
    @DisplayName("Update My Profile - Fail: Patient Record Not Found")
    void updateMyProfile_PatientRecordNotFound() {
        when(accountRepository.findByUsername(patientUsername)).thenReturn(patientAccount);
        when(patientRepository.findByAccountId(patientAccount.getAccountId())).thenReturn(Optional.empty());

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Test");

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            profileService.updateMyProfile(patientUsername, request);
        });

        assertEquals("Không tìm thấy thông tin bệnh nhân", exception.getMessage());
    }

    @Test
    @DisplayName("Update My Profile - Fail: Staff Record Not Found")
    void updateMyProfile_StaffRecordNotFound() {
        when(accountRepository.findByUsername(staffUsername)).thenReturn(staffAccount);
        when(staffRepository.findByAccountId(staffAccount.getAccountId())).thenReturn(Optional.empty());

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Test");

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            profileService.updateMyProfile(staffUsername, request);
        });

        assertEquals("Không tìm thấy thông tin nhân viên", exception.getMessage());
    }

    @Test
    @DisplayName("Update My Profile - Partial Update: Only Update Non-Null Fields")
    void updateMyProfile_PartialUpdate() {
        when(accountRepository.findByUsername(patientUsername)).thenReturn(patientAccount);
        when(patientRepository.findByAccountId(patientAccount.getAccountId())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        UpdateProfileRequest request = new UpdateProfileRequest();
        // Only set fullName, leave other fields null
        request.setFullName("Nguyen Van A Partial");

        ProfileResponse response = profileService.updateMyProfile(patientUsername, request);

        assertNotNull(response);
        // Verify save was called (partial update should still work)
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Update My Profile - Patient Address Update")
    void updateMyProfile_PatientAddressUpdate() {
        when(accountRepository.findByUsername(patientUsername)).thenReturn(patientAccount);
        when(patientRepository.findByAccountId(patientAccount.getAccountId())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setAddress("456 New Street, HCM");

        ProfileResponse response = profileService.updateMyProfile(patientUsername, request);

        assertNotNull(response);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Update My Profile - All Fields Update")
    void updateMyProfile_AllFieldsUpdate() {
        when(accountRepository.findByUsername(patientUsername)).thenReturn(patientAccount);
        when(patientRepository.findByAccountId(patientAccount.getAccountId())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFullName("Nguyen Van A New");
        request.setDateOfBirth(Date.valueOf(LocalDate.of(1992, 8, 25)));
        request.setGender(EGender.MALE);
        request.setPhone("0988888888");
        request.setEmail("new_email@example.com");
        request.setIdCard("111122223333");
        request.setAddress("789 Another St, HCM");

        ProfileResponse response = profileService.updateMyProfile(patientUsername, request);

        assertNotNull(response);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }
}
