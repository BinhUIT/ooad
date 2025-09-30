package com.example.ooad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.TaiKhoan;

@Repository
public interface TaiKhoanRepository extends  JpaRepository<TaiKhoan, Integer>{
    public TaiKhoan findByTenDangNhap(String tenDangNhap);
}
