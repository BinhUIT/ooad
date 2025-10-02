package com.example.ooad.dto.response;

import com.example.ooad.domain.entity.TaiKhoan;
import com.example.ooad.domain.enums.ETrangThaiTaiKhoan;
import com.example.ooad.domain.enums.EVaiTro;

public class TaiKhoanResponse {
    private int maTk;
    private String tenDangNhap;
    private EVaiTro vaiTro;
    private ETrangThaiTaiKhoan trangThai;

    public TaiKhoanResponse(int maTk, String tenDangNhap, ETrangThaiTaiKhoan trangThai, EVaiTro vaiTro) {
        this.maTk = maTk;
        this.tenDangNhap = tenDangNhap;
        this.trangThai = trangThai;
        this.vaiTro = vaiTro;
    }

    public int getMaTk() {
        return maTk;
    }

    public void setMaTk(int maTk) {
        this.maTk = maTk;
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

    public ETrangThaiTaiKhoan getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(ETrangThaiTaiKhoan trangThai) {
        this.trangThai = trangThai;
    }

    public TaiKhoanResponse() {
    }
    public TaiKhoanResponse(TaiKhoan taiKhoan) {
        this.maTk= taiKhoan.getMaTK();
        this.tenDangNhap= taiKhoan.getTenDangNhap();
        this.vaiTro= taiKhoan.getVaiTro();
        this.trangThai = taiKhoan.getTrangThai();
    }
}
