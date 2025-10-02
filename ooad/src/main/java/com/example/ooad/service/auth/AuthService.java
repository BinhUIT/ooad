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

import com.example.ooad.domain.entity.TaiKhoan;
import com.example.ooad.dto.request.DangNhapDto;
import com.example.ooad.dto.request.DangXuatDto;
import com.example.ooad.dto.request.TaoTaiKhoanDto;
import com.example.ooad.dto.response.LoginResponse;
import com.example.ooad.dto.response.TaiKhoanResponse;
import com.example.ooad.exception.BadCredentialException;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.exception.UnauthorizedException;
import com.example.ooad.repository.TaiKhoanRepository;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.TaiKhoanValidator;

import jakarta.transaction.Transactional;

@Service
public class AuthService implements UserDetailsService {
    private final TaiKhoanRepository taiKhoanRepo;
    private final TaiKhoanValidator taiKhoanValidator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    
    public AuthService(TaiKhoanRepository taikhoanRepo, TaiKhoanValidator taiKhoanValidator, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authManager, JwtService jwtService) {
        this.taiKhoanRepo= taikhoanRepo;
        this.taiKhoanValidator = taiKhoanValidator;
        this.passwordEncoder= passwordEncoder;
        this.authManager = authManager;
        this.jwtService= jwtService;
    } 
    public TaiKhoanResponse taoTaiKhoan(TaoTaiKhoanDto dto) throws RuntimeException {
        if(taiKhoanValidator.tenTaiKhoanTonTai(dto.getTenDangNhap())){
            throw new ConflictException(Message.tenDangNhapDaDuocSuDung);
        }
        if(!taiKhoanValidator.matKhauHopLe(dto.getMatKhau())) {
            throw new BadRequestException(Message.matKhauKoHopLe);
        }
        String matKhauDaHash = passwordEncoder.encode(dto.getMatKhau());
        TaiKhoan taiKhoanMoi = new TaiKhoan(dto, matKhauDaHash);
        taiKhoanMoi=taiKhoanRepo.save(taiKhoanMoi);
        return new TaiKhoanResponse(taiKhoanMoi);
        
    }

    public LoginResponse dangNhap(DangNhapDto dangNhapDto) throws RuntimeException {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(dangNhapDto.getTenDangNhap(), dangNhapDto.getMatKhau()));
        if(auth.isAuthenticated()) {
            Object userPrincipal = auth.getPrincipal();
            if(userPrincipal instanceof TaiKhoan taiKhoan) {
                String acessToken = jwtService.generateToken(taiKhoan.getTenDangNhap());
                String refreshToken = jwtService.generateRefreshToken(taiKhoan.getTenDangNhap());
                return new LoginResponse(acessToken,refreshToken,new TaiKhoanResponse(taiKhoan));
            }

            throw new RuntimeException(Message.loiServer);
        }
        else {
            throw new BadCredentialException(Message.saiTenDangNhapVaMatKhau);
        }
    }
    @Transactional
    public String dangXuat(DangXuatDto dangXuatDto) {
        jwtService.deleteRefreshToken(dangXuatDto.getRefreshToken());
        jwtService.saveUnusedAccessToken(dangXuatDto.getAcessToken());
        return Message.dangXuatThanhCong;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan taiKhoan=taiKhoanRepo.findByTenDangNhap(username);
        if(taiKhoan==null) {
            throw new UsernameNotFoundException(Message.khongTimThayTaiKhoan);
        }
        return taiKhoan;
    }
    public String generateAcessToken(String refreshToken, String tenTaiKhoan) throws RuntimeException {
        TaiKhoan taiKhoan =(TaiKhoan) loadUserByUsername(tenTaiKhoan);
        if(jwtService.validateRefreshToken(refreshToken, taiKhoan)) {
            String newToken = jwtService.generateToken(tenTaiKhoan);
            return newToken;
        }
        throw new UnauthorizedException(Message.refreshTokenHetHan);
    }
}
