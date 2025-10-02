package com.example.ooad.domain.entity;

import java.sql.Date;

import com.example.ooad.domain.enums.EGioiTinh;
import com.example.ooad.domain.enums.EVaiTro;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="NHANVIEN")
public class NhanVien{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int maNV;
    private String hoTen;
    private Date ngaySinh;
    @Enumerated(EnumType.STRING)
    private EGioiTinh gioiTinh;
    private String sdt;
    @Enumerated(EnumType.STRING)
    private EVaiTro vaiTro;
    @ManyToOne
    @JoinColumn(name="ma_tai_khoan")
    private TaiKhoan taiKhoan;
    public int getMaNV() {
        return maNV;
    }
    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }
    public String getHoTen() {
        return hoTen;
    }
    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    public Date getNgaySinh() {
        return ngaySinh;
    }
    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    public EGioiTinh getGioiTinh() {
        return gioiTinh;
    }
    public void setGioiTinh(EGioiTinh gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
    public String getSdt() {
        return sdt;
    }
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
    public EVaiTro getVaiTro() {
        return vaiTro;
    }
    public void setVaiTro(EVaiTro vaiTro) {
        this.vaiTro = vaiTro;
    }
    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }
    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }
    public NhanVien() {
    }
    public NhanVien(int maNV, String hoTen, Date ngaySinh, EGioiTinh gioiTinh, String sdt, EVaiTro vaiTro,
            TaiKhoan taiKhoan) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.vaiTro = vaiTro;
        this.taiKhoan = taiKhoan;
    }


}
