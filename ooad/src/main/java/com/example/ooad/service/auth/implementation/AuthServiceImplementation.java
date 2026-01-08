package com.example.ooad.service.auth.implementation;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.entity.VerificationCode;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.domain.enums.EStatus;
import com.example.ooad.dto.request.ChangePasswordRequest;
import com.example.ooad.dto.request.CreateAccountDto;
import com.example.ooad.dto.request.CreateActorAccountDto;
import com.example.ooad.dto.request.ForgetPasswordRequest;
import com.example.ooad.dto.request.LoginDto;
import com.example.ooad.dto.request.LogoutDto;
import com.example.ooad.dto.request.RegisterRequest;
import com.example.ooad.dto.request.ResetpasswordRequest;
import com.example.ooad.dto.request.VerifyCodeRequest;
import com.example.ooad.dto.response.AccountResponse;
import com.example.ooad.dto.response.LoginResponse;
import com.example.ooad.dto.response.VerifyCodeResponse;
import com.example.ooad.exception.BadCredentialException;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.exception.UnauthorizedException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.repository.VerificationCodeRepository;
import com.example.ooad.service.auth.interfaces.AuthService;
import com.example.ooad.service.auth.interfaces.JwtService;
import com.example.ooad.service.email.interfaces.EmailService;
import com.example.ooad.utils.DateTimeUtil;
import com.example.ooad.utils.Message;
import com.example.ooad.utils.UUIDGenerator;
import com.example.ooad.validator.AccountValidator;

import jakarta.transaction.Transactional;

@Service
public class AuthServiceImplementation implements UserDetailsService, AuthService {
    private final AccountRepository accountRepo;
    private final AccountValidator taiKhoanValidator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PatientRepository patientRepo;
    private final StaffRepository staffRepo;
    private final VerificationCodeRepository verificationCodeRepo;
    private final EmailService emailService;

    public AuthServiceImplementation(AccountRepository accountRepo, AccountValidator taiKhoanValidator,
            PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authManager,
            JwtService jwtService, PatientRepository patientRepo, StaffRepository staffRepo,
            VerificationCodeRepository verificationCodeRepo, EmailService emailService) {
        this.accountRepo = accountRepo;
        this.taiKhoanValidator = taiKhoanValidator;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.patientRepo = patientRepo;
        this.staffRepo = staffRepo;
        this.verificationCodeRepo = verificationCodeRepo;
        this.emailService = emailService;
    }

    @Override
    public AccountResponse createAccount(CreateAccountDto dto) throws RuntimeException {

        Account newAccount = accountRepo.save(create_account(dto));
        return new AccountResponse(newAccount);

    }

    private Account create_account(CreateAccountDto dto) {
        if (taiKhoanValidator.isNameUsed(dto.getUsername())) {
            throw new ConflictException(Message.tenDangNhapDaDuocSuDung);
        }
        if (!taiKhoanValidator.validPassword(dto.getPassword())) {
            throw new BadRequestException(Message.matKhauKoHopLe);
        }
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        Account newAccount = new Account(dto, hashedPassword);
        return newAccount;
    }

    @Override
    public LoginResponse login(LoginDto dto) throws RuntimeException {
        Authentication auth = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        if (auth.isAuthenticated()) {
            Object userPrincipal = auth.getPrincipal();
            if (userPrincipal instanceof Account account) {
                String acessToken = jwtService.generateToken(account.getUsername());
                String refreshToken = jwtService.generateRefreshToken(account.getUsername());
                return new LoginResponse(acessToken, refreshToken, new AccountResponse(account));
            }

            throw new RuntimeException(Message.loiServer);
        } else {
            throw new BadCredentialException(Message.saiTenDangNhapVaMatKhau);
        }
    }

    @Transactional
    @Override
    public String dangXuat(LogoutDto logoutDto) {
        jwtService.deleteRefreshToken(logoutDto.getRefreshToken());
        jwtService.saveUnusedAccessToken(logoutDto.getAcessToken());
        return Message.dangXuatThanhCong;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account taiKhoan = accountRepo.findByUsername(username);
        if (taiKhoan == null) {
            throw new UsernameNotFoundException(Message.khongTimThayTaiKhoan);
        }
        return taiKhoan;
    }

    @Override
    public String generateAcessToken(String refreshToken, String username) throws RuntimeException {
        Account account = (Account) loadUserByUsername(username);
        if (jwtService.validateRefreshToken(refreshToken, account)) {
            String newToken = jwtService.generateToken(username);
            return newToken;
        }
        throw new UnauthorizedException(Message.refreshTokenHetHan);
    }

    @Override
    public Account getAccountFromAuth(Authentication auth) {
        String username = auth.getName();
        return accountRepo.findByUsername(username);
    }

    @Override
    @Transactional
    public AccountResponse registerPatientAccount(RegisterRequest request) {
        Patient p = patientRepo.findPatientByIdCardOrEmail(request.getIdCard(), request.getEmail());
        if (p != null) {
            throw new ConflictException(Message.patientExist);
        }
        if (!taiKhoanValidator.validPassword(request.getPassword())) {
            throw new BadRequestException(Message.matKhauKoHopLe);
        }
        Patient newPatient = new Patient();
        newPatient.setPhone(request.getPhone());
        newPatient.setFullName(request.getFullName());
        newPatient.setEmail(request.getEmail());
        newPatient.setAddress(request.getAddress());
        newPatient.setFirstVisitDate(null);
        newPatient.setGender(request.getGender());
        newPatient.setRecordCreateDate(Date.valueOf(LocalDate.now()));
        newPatient.setDateOfBirth(request.getDateOfBirth());
        newPatient.setIdCard(request.getIdCard());

        Account account = new Account();
        account.setRole(ERole.PATIENT);
        account.setUserPassword(passwordEncoder.encode(request.getPassword()));
        account.setStatus(EStatus.ACTIVE);
        account.setUsername(request.getFullName());
        account = accountRepo.save(account);

        newPatient.setAccount(account);
        patientRepo.save(newPatient);

        return new AccountResponse(account);

    }

    @Override
    public AccountResponse changePassword(ChangePasswordRequest request, Authentication auth) {
        Account account = this.getAccountFromAuth(auth);
        if (account == null) {
            throw new BadRequestException("Account does not exist");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new BadRequestException("Wrong password");
        }
        account.setUserPassword(passwordEncoder.encode(request.getNewPassword()));
        account = accountRepo.save(account);
        return new AccountResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse linkAccount(CreateActorAccountDto dto) {
        // Try to find Staff first
        Staff staff = staffRepo.findById(dto.getActorId()).orElse(null);

        if (staff != null) {
            // This is a Staff account
            if (staff.getAccount() != null) {
                throw new BadRequestException("This staff already has an account");
            }

            // Automatically determine role from Staff position
            ERole role = mapPositionToRole(staff.getPosition());
            if (role == null) {
                throw new BadRequestException("Invalid staff position: " + staff.getPosition());
            }

            dto.setRole(role);
            Account account = create_account(dto);
            account = accountRepo.save(account);

            staff.setAccount(account);
            staffRepo.save(staff);

            String message = String.format("Successfully created account for %s (Role: %s)",
                    staff.getFullName(), account.getRole());
            return new AccountResponse(account, staff.getFullName(), message);
        }

        // Try to find Patient
        Patient patient = patientRepo.findById(dto.getActorId()).orElse(null);
        if (patient != null) {
            if (patient.getAccount() != null) {
                throw new BadRequestException("This patient already has an account");
            }

            dto.setRole(ERole.PATIENT);
            Account account = create_account(dto);
            account = accountRepo.save(account);

            patient.setAccount(account);
            patientRepo.save(patient);

            String message = String.format("Successfully created account for patient %s",
                    patient.getFullName());
            return new AccountResponse(account, patient.getFullName(), message);
        }

        // Actor not found
        throw new NotFoundException("Staff or Patient with ID " + dto.getActorId() + " not found");
    }

    /**
     * Map Staff position to ERole
     */
    private ERole mapPositionToRole(String position) {
        if (position == null)
            return null;

        String pos = position.toUpperCase();
        if (pos.contains("DOCTOR"))
            return ERole.DOCTOR;
        if (pos.contains("RECEPTIONIST"))
            return ERole.RECEPTIONIST;
        if (pos.contains("WAREHOUSE"))
            return ERole.WAREHOUSE_STAFF;
        if (pos.contains("ADMIN"))
            return ERole.ADMIN;

        return null;
    }

    @Override
    @Transactional
    public boolean sendVerificationEmail(ForgetPasswordRequest request) {
        VerificationCode code = new VerificationCode();
        code.setEmail(request.getEmail());
        code.setCreateAt(new java.util.Date());
        code.setCode(UUIDGenerator.generateRandomCode());
        List<VerificationCode> listCodes = verificationCodeRepo.findByEmail(request.getEmail());
        verificationCodeRepo.deleteAll(listCodes);
        verificationCodeRepo.save(code);
        emailService.sendCode(code.getCode(), request.getEmail());
        return true;

    }

    @Override
    public VerifyCodeResponse verifyCode(VerifyCodeRequest request) {
        VerificationCode verificationCode = verificationCodeRepo.findByCode(request.getCode());
        if (verificationCode == null) {
            throw new BadRequestException("Wrong code");
        }
        if (DateTimeUtil.isCodeExpire(verificationCode)) {
            throw new BadRequestException("Code expired");
        }
        if (!verificationCode.getEmail().equals(request.getEmail())) {
            throw new BadRequestException("Wrong code");
        }
        return new VerifyCodeResponse(request.getCode(), verificationCode.getEmail());
    }

    @Override
    public AccountResponse resetPassword(ResetpasswordRequest request) {
        if (!request.getConfirmNewPassword().equals(request.getNewPassword())) {
            throw new BadRequestException("Password and confirm password does not match");
        }
        verifyCode(new VerifyCodeRequest(request.getVerificationCode(), request.getEmail()));
        Account account = findByEmail(request.getEmail());
        account.setUserPassword(passwordEncoder.encode(request.getNewPassword()));
        account = accountRepo.save(account);
        return new AccountResponse(account);
    }

    @Override
    public Account findByEmail(String email) {
        Patient p = patientRepo.findByEmail(email);
        if (p != null) {
            return p.getAccount();
        }
        Staff s = staffRepo.findByEmail(email);
        if (s == null)
            return null;
        return s.getAccount();
    }
}
