package com.example.ooad.controller.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ooad.dto.request.UpdateProfileRequest;
import com.example.ooad.dto.response.GlobalResponse;
import com.example.ooad.dto.response.ProfileResponse;
import com.example.ooad.exception.BadRequestException;
import com.example.ooad.service.profile.interfaces.ProfileService;
import com.example.ooad.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller xử lý API liên quan đến thông tin cá nhân (profile).
 * 
 * SỬ DỤNG NGUYÊN TẮC BẢO MẬT CHỐNG IDOR:
 * - Không dùng ID từ URL hay body request
 * - Lấy identity từ JWT Token thông qua @AuthenticationPrincipal
 * - User chỉ có thể xem/sửa thông tin của chính mình
 */
@RestController
@RequestMapping("/account")
@Tag(name = "Profile Management", description = "API quản lý thông tin cá nhân - Bảo mật chống IDOR")
public class ProfileController {
    
    private final ProfileService profileService;
    
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    
    /**
     * Lấy thông tin profile của user hiện tại.
     * Username được lấy từ JWT Token, không từ client.
     */
    @GetMapping("/me")
    @Operation(
        summary = "Xem thông tin cá nhân",
        description = "Lấy thông tin profile của user đang đăng nhập. Identity được xác định từ JWT Token.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lấy thông tin thành công",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Chưa đăng nhập hoặc token không hợp lệ",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Không tìm thấy thông tin user",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
            )
        }
    )
    public ResponseEntity<GlobalResponse<ProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails currentUser) {
        
        // currentUser.getUsername() lấy từ JWT Token - KHÔNG THỂ GIẢ MẠO
        ProfileResponse result = profileService.getMyProfile(currentUser.getUsername());
        GlobalResponse<ProfileResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Cập nhật thông tin profile của user hiện tại.
     * Username được lấy từ JWT Token, không từ client.
     * Chỉ cho phép cập nhật các trường an toàn (không bao gồm role, username, password).
     */
    @PutMapping("/me")
    @Operation(
        summary = "Cập nhật thông tin cá nhân",
        description = "Cập nhật profile của user đang đăng nhập. Chỉ cho phép sửa các trường: fullName, dateOfBirth, gender, phone, email, idCard, address. KHÔNG cho phép sửa role, username, password.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cập nhật thành công",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Dữ liệu không hợp lệ",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Chưa đăng nhập hoặc token không hợp lệ",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Không tìm thấy thông tin user",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
            )
        }
    )
    public ResponseEntity<GlobalResponse<ProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody @Valid UpdateProfileRequest request,
            BindingResult bindingResult) {
        
        // Validate request
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        
        // currentUser.getUsername() lấy từ JWT Token - KHÔNG THỂ GIẢ MẠO
        // Kẻ tấn công không thể sửa thông tin của người khác
        ProfileResponse result = profileService.updateMyProfile(currentUser.getUsername(), request);
        GlobalResponse<ProfileResponse> response = new GlobalResponse<>(result, Message.success, 200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
