package com.example.ooad.dto.request;

import java.sql.Date;

import com.example.ooad.domain.enums.EGender;
import com.example.ooad.domain.enums.ERole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StaffRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    private Date dateOfBirth;

    @NotNull(message = "Gender is required")
    private EGender gender;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @NotBlank(message = "ID Card is required")
    @Size(max = 20, message = "ID Card must not exceed 20 characters")
    private String idCard;

    /**
     * @deprecated The `position` field is deprecated. Use `role` (Account.role /
     *             ERole)
     *             for authorization and role checks. This field is kept only for
     *             backward compatibility
     *             with existing consumers. TODO: remove this field and related
     *             handling after
     *             external consumers have migrated (monitor for 1-2 sprints).
     */
    @Deprecated
    @Size(max = 50, message = "Position must not exceed 50 characters")
    private String position;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @NotNull(message = "Role is required")
    private ERole role;

    public StaffRequest() {
        this.isActive = true;
    }

    public StaffRequest(String fullName, Date dateOfBirth, EGender gender, String email, String phone,
            String idCard, String position, Boolean isActive, ERole role) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.idCard = idCard;
        this.position = position;
        this.isActive = isActive != null ? isActive : true;
        this.role = role;
    }

    // Getters and Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }
}
