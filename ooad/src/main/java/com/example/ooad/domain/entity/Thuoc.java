package com.example.ooad.domain.entity;

import com.example.ooad.domain.enums.EDonViTinhThuoc;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="THUOC")
public class Thuoc {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int maThuoc;
    private String tenThuoc;
    @Enumerated(EnumType.STRING)
    private EDonViTinhThuoc donViTinh;
    private String hamLuong;
    private String dangBaoChe;
    private String nhaSanXuat;
    private String cachDung;
    private String hinhAnh;

    public Thuoc() {
    }
    public int getMaThuoc() {
        return maThuoc;
    }
    public void setMaThuoc(int maThuoc) {
        this.maThuoc = maThuoc;
    }
    public String getTenThuoc() {
        return tenThuoc;
    }
    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }
    public EDonViTinhThuoc getDonViTinh() {
        return donViTinh;
    }
    public void setDonViTinh(EDonViTinhThuoc donViTinh) {
        this.donViTinh = donViTinh;
    }
    public String getHamLuong() {
        return hamLuong;
    }
    public void setHamLuong(String hamLuong) {
        this.hamLuong = hamLuong;
    }
    public String getDangBaoChe() {
        return dangBaoChe;
    }
    public void setDangBaoChe(String dangBaoChe) {
        this.dangBaoChe = dangBaoChe;
    }
    public String getNhaSanXuat() {
        return nhaSanXuat;
    }
    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }
    public String getCachDung() {
        return cachDung;
    }
    public void setCachDung(String cachDung) {
        this.cachDung = cachDung;
    }
    public String getHinhAnh() {
        return hinhAnh;
    }
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    public Thuoc(int maThuoc, String tenThuoc, EDonViTinhThuoc donViTinh, String hamLuong, String dangBaoChe,
            String nhaSanXuat, String cachDung, String hinhAnh) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.donViTinh = donViTinh;
        this.hamLuong = hamLuong;
        this.dangBaoChe = dangBaoChe;
        this.nhaSanXuat = nhaSanXuat;
        this.cachDung = cachDung;
        this.hinhAnh = hinhAnh;
    }
    
}
