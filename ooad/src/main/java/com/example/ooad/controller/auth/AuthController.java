package com.example.ooad.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.dto.request.CreateAccountDto;
import com.example.ooad.dto.request.GetAcessTokenDto;
import com.example.ooad.dto.request.LoginDto;
import com.example.ooad.dto.request.LogoutDto;
import com.example.ooad.dto.response.AccountResponse;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.LoginResponse;
import com.example.ooad.service.auth.AuthService;
import com.example.ooad.utils.Message;

@RestController
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    } 
    @PostMapping("/auth/create_account")
    public ResponseEntity<GlobalResponse<Account>> taoTaiKhoan(@RequestBody CreateAccountDto dto) {
        AccountResponse taiKhoan = authService.createAccount(dto);
        GlobalResponse<Account> response = new GlobalResponse(taiKhoan,Message.taoTaiKhoanThanhCong,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<GlobalResponse<LoginResponse>> dangNhap(@RequestBody LoginDto dto) {
        LoginResponse response = authService.login(dto);
        GlobalResponse<LoginResponse> res = new GlobalResponse(response, Message.dangNhapThanhCong, 200);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @PostMapping("/auth/logout")
    public ResponseEntity<GlobalResponse<String>> dangXuat(@RequestBody LogoutDto dto) {
        String message = authService.dangXuat(dto);
        GlobalResponse<String> res= new GlobalResponse(message, Message.dangXuatThanhCong,200);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @PostMapping("/auth/get_access_token") 
    public ResponseEntity<GlobalResponse<String>> layAccessToken(@RequestBody GetAcessTokenDto dto) {
        String token = authService.generateAcessToken(dto.getRefreshToken(), dto.getUsername());
        GlobalResponse<String> res = new GlobalResponse(token,Message.thanhCong,200);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
