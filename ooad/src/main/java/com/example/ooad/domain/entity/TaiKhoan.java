package com.example.ooad.domain.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.ooad.domain.enums.ETrangThaiTaiKhoan;
import com.example.ooad.domain.enums.EVaiTro;
import com.example.ooad.dto.request.TaoTaiKhoanDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="TAIKHOAN")
public class TaiKhoan implements UserDetails {
    private static final String AUTHORITIES_DELIMETER ="::";
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int maTK;
    @Column(unique=true)
    private String tenDangNhap;
    private String matKhau;
    @Enumerated(EnumType.STRING)
    private EVaiTro vaiTro;
    @Enumerated(EnumType.STRING)
    private ETrangThaiTaiKhoan trangThai;
    /*@OneToOne(mappedBy="taiKhoan")
    @JsonIgnore
    private NhanVien nhanVien;*/
    public TaiKhoan() {
    }
    public TaiKhoan(TaoTaiKhoanDto dto, String matKhau) {
        this.tenDangNhap= dto.getTenDangNhap();
        this.matKhau=matKhau;
        this.vaiTro=dto.getVaiTro();
        this.trangThai=ETrangThaiTaiKhoan.DANGSUDUNG;
    }
    public TaiKhoan(int maTK, String tenDangNhap, String matKhau, EVaiTro vaiTro, ETrangThaiTaiKhoan trangThai /*NhanVien nhanVien*/) {
        this.maTK = maTK;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        //this.nhanVien= nhanVien;
        this.trangThai = trangThai; 
    }
    /*@JsonIgnore
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }*/
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
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authorities = this.vaiTro.getLabel();
        return Arrays.stream(authorities.split(AUTHORITIES_DELIMETER)).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return this.matKhau;
    }
    @Override
    public String getUsername() {
        return this.tenDangNhap;
    }
    
    
}
