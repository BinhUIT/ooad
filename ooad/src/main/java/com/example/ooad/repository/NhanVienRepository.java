package com.example.ooad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.NhanVien;
@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    
}
