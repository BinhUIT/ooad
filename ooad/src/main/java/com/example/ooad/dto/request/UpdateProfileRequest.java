package com.example.ooad.dto.request;

import java.sql.Date;

import com.example.ooad.domain.enums.EGender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO để cập nhật thông tin cá nhân.
 * KHÔNG chứa các trường nhạy cảm như: role, username, password, accountId
 * để ngăn chặn việc user tự nâng quyền hoặc thay đổi identity.
 */
public class UpdateProfileRequest {
    
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2 đến 100 ký tự")
    private String fullName;
    
    private Date dateOfBirth;
    
    private EGender gender;
    
    @Pattern(regexp = "^(0[0-9]{9,10})?$", message = "Số điện thoại không hợp lệ")
    private String phone;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    @Pattern(regexp = "^([0-9]{9}|[0-9]{12})?$", message = "CMND/CCCD phải có 9 hoặc 12 số")
    private String idCard;
    
    // Chỉ dành cho Patient
    private String address;

    public UpdateProfileRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
