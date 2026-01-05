package com.example.ooad.service.account.implementation;

import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.service.account.interfaces.AccountService;
@Service
public class AccountServiceImplementation implements AccountService{
    private final AccountRepository accountRepo;
    private final StaffRepository staffRepo;
    public AccountServiceImplementation(AccountRepository accountRepo, StaffRepository staffRepo) {
        this.accountRepo = accountRepo;
        this.staffRepo = staffRepo;
    }
    @Override
    public Staff getStaffIdFromAccountName(String accountName) {
        Account account = accountRepo.findByUsername(accountName);
        if(account==null) {
            throw new NotFoundException("Account not found");
        }
        return staffRepo.findByAccount_AccountId(account.getAccountId());
    }
    
}
