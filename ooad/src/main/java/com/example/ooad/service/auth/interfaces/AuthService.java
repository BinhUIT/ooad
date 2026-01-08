package com.example.ooad.service.auth.interfaces;

import org.springframework.security.core.Authentication;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.dto.request.ChangePasswordRequest;
import com.example.ooad.dto.request.CreateAccountDto;
import com.example.ooad.dto.request.CreateActorAccountDto;
import com.example.ooad.dto.request.ForgetPasswordRequest;
import com.example.ooad.dto.request.LoginDto;
import com.example.ooad.dto.request.LogoutDto;
import com.example.ooad.dto.request.RegisterRequest;
import com.example.ooad.dto.request.ResetpasswordRequest;
import com.example.ooad.dto.request.VerifyCodeRequest;
import com.example.ooad.dto.response.AccountResponse;
import com.example.ooad.dto.response.LoginResponse;
import com.example.ooad.dto.response.VerifyCodeResponse;

public interface AuthService {
    public AccountResponse createAccount(CreateAccountDto dto);

    public LoginResponse login(LoginDto dto);

    public String dangXuat(LogoutDto logoutDto);

    public String generateAcessToken(String refreshToken, String username);

    public Account getAccountFromAuth(Authentication auth);

    public AccountResponse registerPatientAccount(RegisterRequest request);

    public AccountResponse changePassword(ChangePasswordRequest request, Authentication auth);

    public AccountResponse linkAccount(CreateActorAccountDto dto);

    public boolean sendVerificationEmail(ForgetPasswordRequest request);

    public VerifyCodeResponse verifyCode(VerifyCodeRequest request);

    public AccountResponse resetPassword(ResetpasswordRequest request);

    public Account findByEmail(String email);
}
