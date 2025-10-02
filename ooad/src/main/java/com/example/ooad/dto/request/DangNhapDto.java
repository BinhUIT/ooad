package com.example.ooad.dto.request;

public class DangNhapDto {
    private String tenDangNhap;
    private String matKhau;

    public DangNhapDto() {
    }

    public DangNhapDto(String matKhau, String tenDangNhap) {
        this.matKhau = matKhau;
        this.tenDangNhap = tenDangNhap;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    
}
