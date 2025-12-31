package com.example.ooad.controller.auth;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.domain.entity.Account;
import com.example.ooad.dto.request.ChangePasswordRequest;
import com.example.ooad.dto.request.CreateAccountDto;
import com.example.ooad.dto.request.CreateActorAccountDto;
import com.example.ooad.dto.request.ForgetPasswordRequest;
import com.example.ooad.dto.request.GetAcessTokenDto;
import com.example.ooad.dto.request.LoginDto;
import com.example.ooad.dto.request.LogoutDto;
import com.example.ooad.dto.request.RegisterRequest;
import com.example.ooad.dto.request.ResetpasswordRequest;
import com.example.ooad.dto.response.AccountResponse;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.LoginResponse;
import com.example.ooad.dto.response.VerifyCodeResponse;
import com.example.ooad.service.auth.interfaces.AuthService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="Authentication")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    } 
    @PostMapping("/auth/create_account")
    @Operation(
        description="Create Account",
        responses={
            @ApiResponse(
                description=Message.matKhauKoHopLe,
                responseCode="400",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
             @ApiResponse(
                description=Message.tenDangNhapDaDuocSuDung,
                responseCode="409",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
            @ApiResponse(
                description="Success",
                content = @Content(
                    mediaType="application/json"
                )
            )
        }
    )
    public ResponseEntity<GlobalResponse<Account>> taoTaiKhoan(@RequestBody CreateAccountDto dto) {
        AccountResponse taiKhoan = authService.createAccount(dto);
        GlobalResponse<Account> response = new GlobalResponse(taiKhoan,Message.taoTaiKhoanThanhCong,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    @Operation(
        description="Create Schedule",
        responses={
            @ApiResponse(
                description=Message.loiServer,
                responseCode="500",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
             @ApiResponse(
                description=Message.saiTenDangNhapVaMatKhau,
                responseCode="400",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
            @ApiResponse(
                description="Success",
                content = @Content(
                    mediaType="application/json"
                )
            )
        }
    )
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
    @Operation(
        description="Create Schedule",
        responses={
            @ApiResponse(
                description=Message.refreshTokenHetHan,
                responseCode="401",
                content = @Content(
        
        mediaType = "application/json",
        schema = @Schema(implementation = GlobalResponse.class) 
       
    )
            ),
            @ApiResponse(
                description="Success",
                content = @Content(
                    mediaType="application/json"
                )
            )
        }
    )
    public ResponseEntity<GlobalResponse<String>> layAccessToken(@RequestBody GetAcessTokenDto dto) {
        String token = authService.generateAcessToken(dto.getRefreshToken(), dto.getUsername());
        GlobalResponse<String> res = new GlobalResponse(token,Message.thanhCong,200);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/auth/patient/register")
    public ResponseEntity<GlobalResponse<AccountResponse>> patientRegister(@RequestBody RegisterRequest request) {
        AccountResponse result = authService.registerPatientAccount(request);
        GlobalResponse<AccountResponse> response= new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/change_password")
    public ResponseEntity<GlobalResponse<AccountResponse>> changePassword(@RequestBody ChangePasswordRequest request, Authentication auth) {
        AccountResponse result = authService.changePassword(request, auth);
        GlobalResponse<AccountResponse> response= new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/link_account")
    public ResponseEntity<GlobalResponse<AccountResponse>> linkAccount(@RequestBody CreateActorAccountDto dto) {
        AccountResponse result = authService.linkAccount(dto);
        GlobalResponse<AccountResponse> response= new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/forget_password")
    public ResponseEntity<GlobalResponse<String>> sendResetCode(@RequestBody ForgetPasswordRequest request) {
        authService.sendVerificationEmail(request);
        GlobalResponse<String> response = new GlobalResponse<>(Message.success, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/verify_code")
    public ResponseEntity<GlobalResponse<VerifyCodeResponse>> verifyCode(@RequestParam(defaultValue="") String code) {
        VerifyCodeResponse result = authService.verifyCode(code);
        GlobalResponse<VerifyCodeResponse> response = new GlobalResponse<>(result, Message.success,200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth/reset_password")
    public ResponseEntity<GlobalResponse<AccountResponse>> resetPassword(@RequestBody ResetpasswordRequest request) {
        AccountResponse result = authService.resetPassword(request);
        GlobalResponse<AccountResponse> response= new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
