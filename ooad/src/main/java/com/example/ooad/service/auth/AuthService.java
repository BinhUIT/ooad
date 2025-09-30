package com.example.ooad.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.TaiKhoan;
import com.example.ooad.dto.request.TaoTaiKhoanDto;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.ConflictException;
import com.example.ooad.repository.TaiKhoanRepository;
import com.example.ooad.utils.Message;
import com.example.ooad.validator.TaiKhoanValidator;

@Service
public class AuthService {
    private final TaiKhoanRepository taiKhoanRepo;
    private final TaiKhoanValidator taiKhoanValidator;
    private final PasswordEncoder passwordEncoder;
    public AuthService(TaiKhoanRepository taikhoanRepo, TaiKhoanValidator taiKhoanValidator, PasswordEncoder passwordEncoder) {
        this.taiKhoanRepo= taikhoanRepo;
        this.taiKhoanValidator = taiKhoanValidator;
        this.passwordEncoder= passwordEncoder;
    } 
    public TaiKhoan taoTaiKhoan(TaoTaiKhoanDto dto) throws RuntimeException {
        if(taiKhoanValidator.tenTaiKhoanTonTai(dto.getTenDangNhap())){
            throw new ConflictException(Message.tenDangNhapDaDuocSuDung);
        }
        if(!taiKhoanValidator.matKhauHopLe(dto.getMatKhau())) {
            throw new BadRequestException(Message.matKhauKoHopLe);
        }
        String matKhauDaHash = passwordEncoder.encode(dto.getMatKhau());
        TaiKhoan taiKhoanMoi = new TaiKhoan(dto, matKhauDaHash);
        taiKhoanMoi=taiKhoanRepo.save(taiKhoanMoi);
        taiKhoanMoi.setMatKhau("");
        return taiKhoanMoi;
        
    }
}
