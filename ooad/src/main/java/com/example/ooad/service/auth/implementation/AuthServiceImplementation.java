package com.example.ooad.service.auth.implementation;

import java.sql.Date;
import java.time.LocalDate;

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
import com.example.ooad.domain.entity.Actor;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.domain.enums.EStatus;
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
import com.example.ooad.service.auth.interfaces.AuthService;
import com.example.ooad.service.auth.interfaces.JwtService;
import com.example.ooad.utils.Message;
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
    public AuthServiceImplementation(AccountRepository accountRepo, AccountValidator taiKhoanValidator, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authManager,
         JwtService jwtService, PatientRepository patientRepo, StaffRepository staffRepo) {
        this.accountRepo= accountRepo;
        this.taiKhoanValidator = taiKhoanValidator;
        this.passwordEncoder= passwordEncoder;
        this.authManager = authManager;
        this.jwtService= jwtService;
        this.patientRepo = patientRepo;
        this.staffRepo = staffRepo;
    } 
    @Override
    public AccountResponse createAccount(CreateAccountDto dto) throws RuntimeException {
        
        Account newAccount= accountRepo.save(create_account(dto));
        return new AccountResponse(newAccount);
        
    }
    private Account create_account(CreateAccountDto dto) {
        if(taiKhoanValidator.isNameUsed(dto.getUsername())){
            throw new ConflictException(Message.tenDangNhapDaDuocSuDung);
        }
        if(!taiKhoanValidator.validPassword(dto.getPassword())) {
            throw new BadRequestException(Message.matKhauKoHopLe);
        }
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        Account newAccount = new Account(dto, hashedPassword);
        return newAccount;
    }
    @Override
    public LoginResponse login(LoginDto dto) throws RuntimeException {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        if(auth.isAuthenticated()) {
            Object userPrincipal = auth.getPrincipal();
            if(userPrincipal instanceof Account account) {
                String acessToken = jwtService.generateToken(account.getUsername());
                String refreshToken = jwtService.generateRefreshToken(account.getUsername());
                return new LoginResponse(acessToken,refreshToken,new AccountResponse(account));
            }

            throw new RuntimeException(Message.loiServer);
        }
        else {
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
        Account taiKhoan=accountRepo.findByUsername(username);
        if(taiKhoan==null) {
            throw new UsernameNotFoundException(Message.khongTimThayTaiKhoan);
        }
        return taiKhoan;
    }
    @Override
    public String generateAcessToken(String refreshToken, String username) throws RuntimeException {
        Account account =(Account) loadUserByUsername(username);
        if(jwtService.validateRefreshToken(refreshToken, account)) {
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
        if(p!=null) {
            throw new ConflictException(Message.patientExist);
        } 
        if(!taiKhoanValidator.validPassword(request.getPassword())) {
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
        if(account==null) {
            throw new BadRequestException("Account does not exist"); 
        }
        if(!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new BadRequestException("Wrong password");
        } 
        account.setUserPassword(passwordEncoder.encode(request.getNewPassword()));
        account=accountRepo.save(account);
        return new AccountResponse(account);
    }
    @Override
    @Transactional
    public AccountResponse linkAccount(CreateActorAccountDto dto) {
        Account account = create_account(dto);
        account = accountRepo.save(account);
        if(dto.getRole()!=ERole.PATIENT) {
            Staff staff = staffRepo.findById(dto.getActorId()).orElseThrow(()->new NotFoundException("Staff not found"));
            if(staff.getAccount()!=null) {
                throw new BadRequestException("Can not link account");
            }
            
            switch (dto.getRole()) {
                case RECEPTIONIST:
                    if(!staff.getPosition().equals("Receptionist")) {
                        throw new BadRequestException("Role is not correct");
                    }
                    break;
            
                case WAREHOUSE_STAFF: 
                    if(staff.getPosition().equals("Warehouse-staff")) {
                        throw new BadRequestException("Role is not correct");
                    }
                    break;
                case DOCTOR: 
                    if(staff.getPosition().equals("Doctor")) {
                            throw new BadRequestException("Role is not correct");
                    }
                    break;
            }
            staff.setAccount(account);
            staffRepo.save(staff);
            return new AccountResponse(account);
        }
        Patient p = patientRepo.findById(dto.getActorId()).orElseThrow(()->new NotFoundException("Patient not found"));
        if(p.getAccount()!=null) {
            throw new BadRequestException("Can not link account");
        }
        p.setAccount(account);
        patientRepo.save(p);
        return new AccountResponse(account);

        
    }
}
