package com.example.ooad.service.auth.implementation;import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.enums.EGender;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.dto.request.ChangePasswordRequest;
import com.example.ooad.dto.request.CreateAccountDto;
import com.example.ooad.dto.request.CreateActorAccountDto;
import com.example.ooad.dto.request.LoginDto;
import com.example.ooad.dto.request.LogoutDto;
import com.example.ooad.dto.request.RegisterRequest;
import com.example.ooad.dto.response.AccountResponse;
import com.example.ooad.dto.response.LoginResponse;
import com.example.ooad.exception.BadCredentialException;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.exception.UnauthorizedException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.repository.VerificationCodeRepository;
import com.example.ooad.service.auth.interfaces.JwtService;
import com.example.ooad.service.email.interfaces.EmailService;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.AccountValidator;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private AccountRepository accountRepo;
    @Mock
    private AccountValidator taiKhoanValidator;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  AuthenticationManager authManager;
    @Mock
    private  JwtService jwtService;
    @Mock
    private  PatientRepository patientRepo;
    @Mock
    private  StaffRepository staffRepo;
    @Mock
    private  VerificationCodeRepository verificationCodeRepo;
    @Mock
    private  EmailService emailService;
    @Mock
    private Authentication auth;

    @InjectMocks
    private AuthServiceImplementation authService;

    @Test
    void createAccountSuccess() {
        Account fakeAccount = new Account();

        when(taiKhoanValidator.isNameUsed(any(String.class))).thenReturn(false);
        when(taiKhoanValidator.validPassword(any(String.class))).thenReturn(true);
        when(passwordEncoder.encode(any(String.class))).thenReturn("");
        when(accountRepo.save(any(Account.class))).thenReturn(fakeAccount);

        AccountResponse result = authService.createAccount(new CreateAccountDto("",ERole.PATIENT,""));

        assertNotNull(result);
        verify(passwordEncoder,times(1)).encode(any(String.class));
        verify(accountRepo,times(1)).save(any(Account.class));
    }

    @Test
    void createAccountFail_NameUsed() {
        when(taiKhoanValidator.isNameUsed(any(String.class))).thenReturn(true);
        ConflictException exception = assertThrows(ConflictException.class, ()->{
            authService.createAccount(new CreateAccountDto("",ERole.PATIENT,""));
        });

        assertNotNull(exception);
        assertEquals(Message.tenDangNhapDaDuocSuDung, exception.getMessage());
        verify(accountRepo,never()).save(any(Account.class));
    }

    @Test
    void createAccountFail_InvalidPassword() {
        when(taiKhoanValidator.isNameUsed(any(String.class))).thenReturn(false);
        when(taiKhoanValidator.validPassword(any(String.class))).thenReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class, ()->{
            authService.createAccount(new CreateAccountDto("",ERole.PATIENT,""));
        });
        assertNotNull(exception);
        assertEquals(Message.matKhauKoHopLe, exception.getMessage());
        verify(accountRepo,never()).save(any(Account.class));
    }

    @Test
    void loginSuccess() {
        Account fakeAccount = new Account();
        fakeAccount.setUsername("");
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(fakeAccount);
        when(jwtService.generateToken(any(String.class))).thenReturn("");
        when(jwtService.generateRefreshToken(any(String.class))).thenReturn("");

        LoginResponse response = authService.login(new LoginDto("", ""));

        assertNotNull(response);
    }

    @Test
    void loginFailWrongUsernameOrPassword() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(false);

        BadCredentialException exception = assertThrows(BadCredentialException.class, ()->{
            authService.login(new LoginDto("", ""));
        });

        assertNotNull(exception);
        assertEquals(Message.saiTenDangNhapVaMatKhau, exception.getMessage());

        verify(jwtService,never()).generateToken(any(String.class));
        verify(jwtService, never()).generateRefreshToken(any(String.class));
    }

    @Test
    void loginFailServerError() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("NotAnObject");

        RuntimeException exception = assertThrows(RuntimeException.class, ()->{
            authService.login(new LoginDto("", ""));
        });

        assertNotNull(exception);
        assertEquals(Message.loiServer, exception.getMessage());

        verify(jwtService,never()).generateToken(any(String.class));
        verify(jwtService, never()).generateRefreshToken(any(String.class));
    }

    @Test
    void logoutTest() {
        doNothing().when(jwtService).deleteRefreshToken(any(String.class));
        doNothing().when(jwtService).saveUnusedAccessToken(any(String.class));

        String message = authService.dangXuat(new LogoutDto("", ""));

        assertNotNull(message);
        assertEquals(Message.dangXuatThanhCong, message);

        verify(jwtService,times(1)).deleteRefreshToken(any(String.class));
        verify(jwtService, times(1)).saveUnusedAccessToken(any(String.class));
    }

    @Test
    void loadUserByUsernameSuccess() {
        UserDetails fakeAccount = new Account();
        when(accountRepo.findByUsername(any(String.class))).thenReturn((Account)fakeAccount);

        UserDetails result = authService.loadUserByUsername("");
        assertNotNull(result);
    }

    @Test
    void loadUserByUsernameFail() {
        when(accountRepo.findByUsername(any(String.class))).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, ()->{
            authService.loadUserByUsername("");
        });
        assertNotNull(exception);
        assertEquals(Message.khongTimThayTaiKhoan, exception.getMessage());
    }
    
    @Test
    void testGenerateAccessTokenSuccess() {
        Account fakeAccount = new Account();
        when(accountRepo.findByUsername(any(String.class))).thenReturn(fakeAccount);
        when(jwtService.validateRefreshToken(any(), any())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("");

        String accessToken = authService.generateAcessToken("", "");
        assertNotNull(accessToken);
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void testGenerateAccessTokenFail() {
        Account fakeAccount = new Account();
        when(accountRepo.findByUsername(any(String.class))).thenReturn(fakeAccount);
        when(jwtService.validateRefreshToken(any(), any())).thenReturn(false);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, ()->{
            authService.generateAcessToken("", "");
        });

        assertNotNull(exception);
        assertEquals(Message.refreshTokenHetHan, exception.getMessage());
        verify(jwtService,never()).generateToken(any());
    }

    @Test
    void getAccountFromAuthTest() {
        when(auth.getName()).thenReturn("");
        when(accountRepo.findByUsername(any())).thenReturn(new Account());

        Account account = authService.getAccountFromAuth(auth);

        assertNotNull(account);
    }

    @Test
    void registerPatientAccountSuccess() {
        when(patientRepo.findPatientByIdCardOrEmail(any(), any())).thenReturn(null);
        when(taiKhoanValidator.validPassword(any())).thenReturn(true);
        when(accountRepo.save(any())).thenReturn(new Account());
        when(patientRepo.save(any())).thenReturn(new Patient());

        AccountResponse response = authService.registerPatientAccount(new RegisterRequest("", Date.valueOf(LocalDate.now()), EGender.MALE, "", "", "", "", ""));

        assertNotNull(response);

        verify(accountRepo, times(1)).save(any());
        verify(patientRepo, times(1)).save(any());

    }
    @Test
    void registerPatientAccountFail_PatientExist() {
        when(patientRepo.findPatientByIdCardOrEmail(any(), any())).thenReturn(new Patient());

        ConflictException exception = assertThrows(ConflictException.class, ()->{
            authService.registerPatientAccount(new RegisterRequest("", Date.valueOf(LocalDate.now()), EGender.MALE, "", "", "", "", ""));
        });

        assertNotNull(exception);
        assertEquals(Message.patientExist, exception.getMessage());

        verify(accountRepo, never()).save(any());
        verify(patientRepo, never()).save(any());
    }

    @Test
    void registerPatientFail_InvalidPassword() {
        when(patientRepo.findPatientByIdCardOrEmail(any(), any())).thenReturn(null);
        when(taiKhoanValidator.validPassword(any())).thenReturn(false);
        BadRequestException exception = assertThrows(BadRequestException.class,()->{
             authService.registerPatientAccount(new RegisterRequest("", Date.valueOf(LocalDate.now()), EGender.MALE, "", "", "", "", ""));
        });

        assertNotNull(exception);
        assertEquals(Message.matKhauKoHopLe, exception.getMessage());

        verify(accountRepo, never()).save(any());
        verify(patientRepo, never()).save(any());
    }

    @Test
    void changePasswordSuccess() {
        Account fakeAccount = new Account();

        when(auth.getName()).thenReturn("");
        when(accountRepo.findByUsername(any(String.class))).thenReturn(fakeAccount);

        when(passwordEncoder.matches(any(String.class), any())).thenReturn(true);
        when(accountRepo.save(any())).thenReturn(fakeAccount);

        AccountResponse response = authService.changePassword(new ChangePasswordRequest("",""), auth);

        verify(accountRepo, times(1)).save(any());
        assertNotNull(response);


    }

    @Test
    void changePasswordAccountNotFound() {
        when(auth.getName()).thenReturn("");
        when(accountRepo.findByUsername(any(String.class))).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class,()->{
            authService.changePassword(new ChangePasswordRequest("",""), auth);
        });

        assertNotNull(exception);
        assertEquals("Account does not exist", exception.getMessage());

        verify(accountRepo, never()).save(any());
    }

    @Test
    void changePasswordPasswordDoesNotMatch() {
        Account fakeAccount = new Account();

        when(auth.getName()).thenReturn("");
        when(accountRepo.findByUsername(any(String.class))).thenReturn(fakeAccount);

        when(passwordEncoder.matches(any(String.class), any())).thenReturn(false);

         BadRequestException exception = assertThrows(BadRequestException.class,()->{
            authService.changePassword(new ChangePasswordRequest("",""), auth);
        });

        assertNotNull(exception);
        assertEquals("Wrong password", exception.getMessage());

        verify(accountRepo, never()).save(any());
    }

    @Test
    void linkAccountPatientSuccess() {

        Patient fakePatient = new Patient();
        Account newAccount = new Account();
        newAccount.setUsername("");
        when(taiKhoanValidator.isNameUsed(any(String.class))).thenReturn(false);
        when(taiKhoanValidator.validPassword(any(String.class))).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("");
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(fakePatient));
        when(patientRepo.save(any())).thenReturn(fakePatient);
        when(accountRepo.save(any())).thenReturn(newAccount);
        AccountResponse response = authService.linkAccount(new CreateActorAccountDto(1,"",ERole.PATIENT,""));
        assertNotNull(response);
        verify(patientRepo, times(1)).save(any());
        verify(accountRepo, times(1)).save(any());
        verify(staffRepo, never()).save(any());
    }
    @Test
    void linkAccountPatientFailPatientNotFound() {
        Account newAccount = new Account();
        newAccount.setUsername("");
        when(taiKhoanValidator.isNameUsed(any(String.class))).thenReturn(false);
        when(taiKhoanValidator.validPassword(any(String.class))).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("");
        when(patientRepo.findById(anyInt())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,()->{
            authService.linkAccount(new CreateActorAccountDto(1,"",ERole.PATIENT,""));
        });

        assertNotNull(exception);
        assertEquals("Patient not found",exception.getMessage());

        verify(patientRepo, never()).save(any());
        
        verify(staffRepo, never()).save(any());
    }

    @Test
    void linkAccountPatientFailPatientHadAccount() {
        Patient fakePatient = new Patient();
        Account newAccount = new Account();
        fakePatient.setAccount(newAccount);
        newAccount.setUsername("");
        when(taiKhoanValidator.isNameUsed(any(String.class))).thenReturn(false);
        when(taiKhoanValidator.validPassword(any(String.class))).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("");
        when(patientRepo.findById(anyInt())).thenReturn(Optional.of(fakePatient));
        when(accountRepo.save(any())).thenReturn(newAccount);

        BadRequestException exception = assertThrows(BadRequestException.class, ()->{
            authService.linkAccount(new CreateActorAccountDto(1,"",ERole.PATIENT,""));
        });

         assertNotNull(exception);
        assertEquals("Can not link account",exception.getMessage());

        verify(patientRepo, never()).save(any());
        
        verify(staffRepo, never()).save(any());

    }
}
