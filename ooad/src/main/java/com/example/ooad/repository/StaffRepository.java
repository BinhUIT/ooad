package com.example.ooad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    public Staff findByAccount_AccountId(int accountId);    
    /**
     * Tìm Staff theo Account ID (dùng cho API profile /me)
     */
    @Query("SELECT s FROM Staff s WHERE s.account.accountId = :accountId")
    Optional<Staff> findByAccountId(@Param("accountId") int accountId);
}
