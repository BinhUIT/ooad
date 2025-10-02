package com.example.ooad.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.TaiKhoan;
import com.example.ooad.dto.request.DangNhapDto;
import com.example.ooad.dto.request.DangXuatDto;
import com.example.ooad.dto.request.LayAcessTokenDto;
import com.example.ooad.dto.request.TaoTaiKhoanDto;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.LoginResponse;
import com.example.ooad.dto.response.TaiKhoanResponse;
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
        TaiKhoanResponse taiKhoan = authService.taoTaiKhoan(dto);
        GlobalResponse<TaiKhoan> response = new GlobalResponse(taiKhoan,Message.taoTaiKhoanThanhCong,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/dang_nhap")
    public ResponseEntity<GlobalResponse<LoginResponse>> dangNhap(@RequestBody DangNhapDto dto) {
        LoginResponse response = authService.dangNhap(dto);
        GlobalResponse<LoginResponse> res = new GlobalResponse(response, Message.dangNhapThanhCong, 200);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @PostMapping("/auth/dang_xuat")
    public ResponseEntity<GlobalResponse<String>> dangXuat(@RequestBody DangXuatDto dto) {
        String message = authService.dangXuat(dto);
        GlobalResponse<String> res= new GlobalResponse(message, Message.dangXuatThanhCong,200);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @PostMapping("/auth/lay_access_token") 
    public ResponseEntity<GlobalResponse<String>> layAccessToken(@RequestBody LayAcessTokenDto dto) {
        String token = authService.generateAcessToken(dto.getRefreshToken(), dto.getTenTaiKhoan());
        GlobalResponse<String> res = new GlobalResponse(token,Message.thanhCong,200);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
