package com.example.ooad.service.profile.interfaces;

import com.example.ooad.dto.request.UpdateProfileRequest;
import com.example.ooad.dto.response.ProfileResponse;

/**
 * Service xử lý thông tin cá nhân (profile) của user.
 * Sử dụng username từ JWT Token để xác định user, không dùng ID từ client.
 */
public interface ProfileService {
    
    /**
     * Lấy thông tin profile của user hiện tại
     * @param username Username từ JWT Token
     * @return ProfileResponse chứa thông tin user
     */
    ProfileResponse getMyProfile(String username);
    
    /**
     * Cập nhật thông tin profile của user hiện tại
     * @param username Username từ JWT Token
     * @param request DTO chứa thông tin cần cập nhật
     * @return ProfileResponse chứa thông tin sau khi cập nhật
     */
    ProfileResponse updateMyProfile(String username, UpdateProfileRequest request);
}
