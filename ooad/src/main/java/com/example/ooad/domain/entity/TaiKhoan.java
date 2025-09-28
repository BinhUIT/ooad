package com.example.ooad.domain.entity;

import com.example.ooad.domain.enums.ETrangThaiTaiKhoan;
import com.example.ooad.domain.enums.EVaiTro;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="TAIKHOAN")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int maTK;
    private String tenDangNhap;
    private String matKhau;
    @Enumerated(EnumType.STRING)
    private EVaiTro vaiTro;
    @Enumerated(EnumType.STRING)
    private ETrangThaiTaiKhoan trangThai;
    @OneToOne(mappedBy="taiKhoan")
    @JsonIgnore
    private NhanVien nhanVien;
    public TaiKhoan() {
    }
    public TaiKhoan(int maTK, String tenDangNhap, String matKhau, EVaiTro vaiTro, ETrangThaiTaiKhoan trangThai, NhanVien nhanVien) {
        this.maTK = maTK;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.nhanVien= nhanVien;
        this.trangThai = trangThai;
    }
    @JsonIgnore
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    public int getMaTK() {
        return maTK;
    }
    public void setMaTK(int maTK) {
        this.maTK = maTK;
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
    public EVaiTro getVaiTro() {
        return vaiTro;
    }
    public void setVaiTro(EVaiTro vaiTro) {
        this.vaiTro = vaiTro;
    }
    public ETrangThaiTaiKhoan getTrangThai() {
        return trangThai;
    }
    public void setTrangThai(ETrangThaiTaiKhoan trangThai) {
        this.trangThai = trangThai;
    }
    
}
