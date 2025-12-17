package com.example.ooad.service.reception.implementation;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.domain.entity.Patient;
import com.example.ooad.domain.entity.Reception;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EReceptionStatus;
import com.example.ooad.domain.enums.ERole;
import com.example.ooad.dto.request.CreateReceptionRequest;
import com.example.ooad.dto.request.UpdateReceptionRequest;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.exception.NotFoundException;
import com.example.ooad.repository.PatientRepository;
import com.example.ooad.repository.ReceptionRepository;
import com.example.ooad.utils.Message;
@ExtendWith(MockitoExtension.class)
public class ReceptionServiceImplementationTest {
    @Mock
    private ReceptionRepository receptionRepo;
    @Mock
    private PatientRepository patientRepo;
    @InjectMocks
    private ReceptionServiceImplementation receptionService;
    @Test
    void getListReceptionsNoFilter_Success() {
        Page<Reception> fakeResult = new PageImpl<>(Arrays.asList(new Reception()));
       
        when(receptionRepo.findAllByOrderByReceptionDateDesc(any(Pageable.class))).thenReturn(fakeResult);
        
        Page<Reception> result = receptionService.getListReceptions(7, 10, Optional.empty(), Optional.empty());

        assertNotNull(result);
        assertNotEquals(result.getContent().size(),0);
        
    }
    @Test
    void getReceptionById_Success() {
        Reception fakeResult = new Reception();
        fakeResult.setReceptionId(1);
        when(receptionRepo.findById(1)).thenReturn(Optional.of(fakeResult));
        Reception reception = receptionService.getReceptionById(1);
        assertNotNull(reception);
        assertEquals(1, reception.getReceptionId());
    }

    @Test
    void getReceptionById_Fail() {
        when(receptionRepo.findById(1)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, ()->{
            receptionService.getReceptionById(1);
        });
        assertNotNull(exception);
        assertEquals(Message.receptionNotFound, exception.getMessage());
        
    }
    @Test
    void editReception_Success() {
        UpdateReceptionRequest fakeRequest = new UpdateReceptionRequest(1,EReceptionStatus.CANCELLED);
        Reception fakeReception= new Reception();
        fakeReception.setReceptionId(1);
        fakeReception.setReceptionDate(Date.valueOf("2025-12-17"));
        
        when(receptionRepo.findById(1)).thenReturn(Optional.of(fakeReception));
        when(receptionRepo.save(any(Reception.class))).thenReturn(fakeReception);

        Reception result = receptionService.editReception(fakeRequest);
        assertNotNull(result);
        assertEquals(fakeReception.getReceptionId(), result.getReceptionId());
        assertEquals(fakeReception.getStatus(),result.getStatus());
        verify(receptionRepo, times(1)).save(any(Reception.class));
    }

    @Test
    void editReception_Fail_InvalidStatus() {
        UpdateReceptionRequest fakeRequest = new UpdateReceptionRequest(1,EReceptionStatus.CANCELLED);
        Reception fakeReception= new Reception();
        fakeReception.setReceptionId(1);
        fakeReception.setReceptionDate(Date.valueOf("2025-12-17"));
        fakeReception.setStatus(EReceptionStatus.CANCELLED);
        when(receptionRepo.findById(1)).thenReturn(Optional.of(fakeReception));
        BadRequestException exception = assertThrows(BadRequestException.class, ()->{
            receptionService.editReception(fakeRequest);
        });
        assertNotNull(exception);
        assertEquals(Message.cannotEditReception, exception.getMessage());
        verify(receptionRepo,never()).save(any(Reception.class));
    }
    @Test
    void editReception_Fail_InvalidDate() {
        UpdateReceptionRequest fakeRequest = new UpdateReceptionRequest(1,EReceptionStatus.CANCELLED);
        Reception fakeReception= new Reception();
        fakeReception.setReceptionId(1);
        fakeReception.setReceptionDate(Date.valueOf("2025-12-16"));
       
        when(receptionRepo.findById(1)).thenReturn(Optional.of(fakeReception));
        BadRequestException exception = assertThrows(BadRequestException.class, ()->{
            receptionService.editReception(fakeRequest);
        });
        assertNotNull(exception);
        assertEquals(Message.cannotEditReception, exception.getMessage());
        verify(receptionRepo,never()).save(any(Reception.class));
    }
    @Test
    void createReceptionSuccess() {
        CreateReceptionRequest request = new CreateReceptionRequest();
        request.setPatientId(1);
        request.setReceptionDate(Date.valueOf("2025-12-17"));
        Staff receptionist = new Staff();
        Account fakeAccount = new Account();
        fakeAccount.setRole(ERole.RECEPTIONIST);
        receptionist.setAccount(fakeAccount);
        Reception fakeResult = new Reception();

        when(patientRepo.findById(1)).thenReturn(Optional.of(new Patient()));
        when(receptionRepo.save(any(Reception.class))).thenReturn(fakeResult);

        Reception result = receptionService.createReception(request, receptionist);

        assertNotNull(result);
        verify(receptionRepo,times(1)).save(any(Reception.class));

    }
    @Test
    void createReception_Fail_NoPermission() {
        CreateReceptionRequest request = new CreateReceptionRequest();
        request.setPatientId(1);
        request.setReceptionDate(Date.valueOf("2025-12-17"));
        Staff receptionist = new Staff();
        Account fakeAccount = new Account();
        fakeAccount.setRole(ERole.WAREHOUSE_STAFF);
        receptionist.setAccount(fakeAccount);
        

        when(patientRepo.findById(1)).thenReturn(Optional.of(new Patient()));

        BadRequestException exception = assertThrows(BadRequestException.class, ()->{
            receptionService.createReception(request, receptionist);
        });

        assertNotNull(exception);
        assertEquals(Message.cannotCreateReception, exception.getMessage());
        verify(receptionRepo, never()).save(any(Reception.class));
    }
    @Test
    void createReception_Fail_InvalidDate() {
        CreateReceptionRequest request = new CreateReceptionRequest();
        request.setPatientId(1);
        request.setReceptionDate(Date.valueOf("2025-12-16"));
        Staff receptionist = new Staff();
        Account fakeAccount = new Account();
        fakeAccount.setRole(ERole.RECEPTIONIST);
        receptionist.setAccount(fakeAccount);

        when(patientRepo.findById(1)).thenReturn(Optional.of(new Patient()));

        BadRequestException exception = assertThrows(BadRequestException.class, ()->{
            receptionService.createReception(request, receptionist);
        });

        assertNotNull(exception);
        assertEquals(Message.invalidReceptionDate, exception.getMessage());
        verify(receptionRepo, never()).save(any(Reception.class));

    }

    @Test
    void createReception_Fail_PatientNotFound() {
        CreateReceptionRequest request = new CreateReceptionRequest();
        request.setPatientId(1);
        request.setReceptionDate(Date.valueOf("2025-12-17"));
        Staff receptionist = new Staff();
        Account fakeAccount = new Account();
        fakeAccount.setRole(ERole.RECEPTIONIST);
        receptionist.setAccount(fakeAccount);

        when(patientRepo.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, ()->{
            receptionService.createReception(request, receptionist);
        });

        assertNotNull(exception);
        assertEquals(Message.patientNotFound, exception.getMessage());
        verify(receptionRepo, never()).save(any(Reception.class));
    }
}
