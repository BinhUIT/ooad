package com.example.ooad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ooad.domain.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    public Staff findByAccount_AccountId(int accountId);
}
