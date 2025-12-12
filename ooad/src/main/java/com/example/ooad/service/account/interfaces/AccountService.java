package com.example.ooad.service.account.interfaces;

import com.example.ooad.domain.entity.Staff;

public interface AccountService {
    public Staff getStaffIdFromAccountName(String accountName);
}
