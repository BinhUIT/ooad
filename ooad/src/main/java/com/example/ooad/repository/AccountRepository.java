package com.example.ooad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Account;
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    public Account findByUsername(String username);
}
