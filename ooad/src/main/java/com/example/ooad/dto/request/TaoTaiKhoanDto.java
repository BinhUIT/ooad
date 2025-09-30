package com.example.ooad.dto.request;

import com.example.ooad.domain.enums.EVaiTro;

public class TaoTaiKhoanDto {
    private String tenDangNhap;
    private EVaiTro vaiTro;
    private String matKhau;

    public TaoTaiKhoanDto(String matKhau, String tenDangNhap, EVaiTro vaiTro) {
        this.matKhau = matKhau;
        this.tenDangNhap = tenDangNhap;
        this.vaiTro = vaiTro;
    }
    public String getTenDangNhap() {
        return tenDangNhap;
    }
    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }
    public EVaiTro getVaiTro() {
        return vaiTro;
    }
    public void setVaiTro(EVaiTro vaiTro) {
        this.vaiTro = vaiTro;
    }
    public String getMatKhau() {
        return matKhau;
    }
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    public TaoTaiKhoanDto() {
    }
    
}
