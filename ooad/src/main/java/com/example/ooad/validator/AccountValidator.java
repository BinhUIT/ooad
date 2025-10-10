package com.example.ooad.validator;

import org.springframework.stereotype.Component;

import com.example.ooad.repository.AccountRepository;

@Component
public class AccountValidator {
    private final AccountRepository accountRepo;
    public AccountValidator(AccountRepository accountRepo) {
        this.accountRepo=accountRepo;
    }
    public boolean isNameUsed(String username) {
        return accountRepo.findByUsername(username)!=null;
    }
    public boolean validPassword(String matKhau) {
        return matKhau.length()>=8;
    }
}
