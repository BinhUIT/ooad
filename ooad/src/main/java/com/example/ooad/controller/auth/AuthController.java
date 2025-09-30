package com.example.ooad.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.TaiKhoan;
import com.example.ooad.dto.request.TaoTaiKhoanDto;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.service.auth.AuthService;
import com.example.ooad.utils.Message;

@RestController
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    } 
    @PostMapping("/auth/tao_tai_khoan")
    public ResponseEntity<GlobalResponse<TaiKhoan>> taoTaiKhoan(@RequestBody TaoTaiKhoanDto dto) {
        
            TaiKhoan taiKhoan = authService.taoTaiKhoan(dto);
            GlobalResponse<TaiKhoan> response = new GlobalResponse(taiKhoan,Message.taoTaiKhoanThanhCong,200);
            return new ResponseEntity<>(response, HttpStatus.OK);
       
    }
}
