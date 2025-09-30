package com.example.ooad.validator;

import org.springframework.stereotype.Component;

import com.example.ooad.repository.TaiKhoanRepository;

@Component
public class TaiKhoanValidator {
    private final TaiKhoanRepository taiKhoanRepo;
    public TaiKhoanValidator(TaiKhoanRepository taiKhoanRepo) {
        this.taiKhoanRepo= taiKhoanRepo;
    }
    public boolean tenTaiKhoanTonTai(String tenTK) {
        return taiKhoanRepo.findByTenDangNhap(tenTK)!=null;
    }
    public boolean matKhauHopLe(String matKhau) {
        return matKhau.length()>=8;
    }
}
