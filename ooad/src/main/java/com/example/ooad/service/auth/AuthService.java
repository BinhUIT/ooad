package com.example.ooad.service.auth;

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
import com.example.ooad.dto.request.CreateAccountDto;
import com.example.ooad.dto.request.LoginDto;
import com.example.ooad.dto.request.LogoutDto;
import com.example.ooad.dto.response.AccountResponse;
import com.example.ooad.dto.response.LoginResponse;
import com.example.ooad.exception.BadCredentialException;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.UnauthorizedException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.AccountValidator;

import jakarta.transaction.Transactional;

@Service
public class AuthService implements UserDetailsService {
    private final AccountRepository accountRepo;
    private final AccountValidator taiKhoanValidator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    
    public AuthService(AccountRepository accountRepo, AccountValidator taiKhoanValidator, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authManager, JwtService jwtService) {
        this.accountRepo= accountRepo;
        this.taiKhoanValidator = taiKhoanValidator;
        this.passwordEncoder= passwordEncoder;
        this.authManager = authManager;
        this.jwtService= jwtService;
    } 
    public AccountResponse createAccount(CreateAccountDto dto) throws RuntimeException {
        if(taiKhoanValidator.isNameUsed(dto.getUsername())){
            throw new ConflictException(Message.tenDangNhapDaDuocSuDung);
        }
        if(!taiKhoanValidator.validPassword(dto.getPassword())) {
            throw new BadRequestException(Message.matKhauKoHopLe);
        }
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        Account newAccount = new Account(dto, hashedPassword);
        newAccount=accountRepo.save(newAccount);
        return new AccountResponse(newAccount);
        
    }

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
    public String generateAcessToken(String refreshToken, String username) throws RuntimeException {
        Account account =(Account) loadUserByUsername(username);
        if(jwtService.validateRefreshToken(refreshToken, account)) {
            String newToken = jwtService.generateToken(username);
            return newToken;
        }
        throw new UnauthorizedException(Message.refreshTokenHetHan);
    }
}
