package com.example.ooad.domain.entity;

import java.sql.Date;

import com.example.ooad.domain.enums.EGioiTinh;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="BENHNHAN")
public class BenhNhan {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int maBN;
    private Date ngaySinh;
    private EGioiTinh gioiTinh;
    private String diaChi;
    private String sdt;
    private String email;
    private String cccd;
    private Date ngayTaoHoSo;

    public int getMaBN() {
        return maBN;
    }

    public void setMaBN(int maBN) {
        this.maBN = maBN;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    

    public Date getNgayTaoHoSo() {
        return ngayTaoHoSo;
    }

    public void setNgayTaoHoSo(Date ngayTaoHoSo) {
        this.ngayTaoHoSo = ngayTaoHoSo;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public BenhNhan() {
    }

    public BenhNhan(int maBN, Date ngaySinh, EGioiTinh gioiTinh, String diaChi, String sdt, String email, String cccd,
            Date ngayTaoHoSo) {
        this.maBN = maBN;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.email = email;
        this.cccd = cccd;
        this.ngayTaoHoSo = ngayTaoHoSo;
    }
    

}
