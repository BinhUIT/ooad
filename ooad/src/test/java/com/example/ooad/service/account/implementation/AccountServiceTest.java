package com.example.ooad.service.account.implementation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.AccountRepository;
import com.example.ooad.repository.StaffRepository;
import com.example.ooad.utils.Message;
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepo;
    @Mock
    private StaffRepository staffRepo;

    @InjectMocks
    private AccountServiceImplementation accountService;

    @Test
    public void testGetStaffIdFromAccountNameSuccess() {
        Account account= new Account();
        account.setAccountId(1);
        Staff staff = new Staff();
        staff.setStaffId(1);
        staff.setAccount(account);

        when(accountRepo.findByUsername(any(String.class))).thenReturn(account);
        when(staffRepo.findByAccount_AccountId(1)).thenReturn(staff);
        
        Staff result = accountService.getStaffIdFromAccountName("");

        assertNotNull(result);
    }

    @Test
    public void testGetStaffIdFromAccountNameFail() {
        when(accountRepo.findByUsername(any(String.class))).thenReturn(null);

         NotFoundException exception = assertThrows(NotFoundException.class, ()->{
            accountService.getStaffIdFromAccountName("");
        });
        assertNotNull(exception);
        assertEquals("Account not found",exception.getMessage());
    }


}
