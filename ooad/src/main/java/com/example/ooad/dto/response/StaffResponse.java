package com.example.ooad.dto.response;

import java.sql.Date;

import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EGender;

public class StaffResponse {
    private int staffId;
    private String fullName;
    private Date dateOfBirth;
    private EGender gender;
    private String email;
    private String phone;
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
    private String position;
    private String role; // From Account role
    private Boolean active;

    public StaffResponse() {
    }

    public StaffResponse(Staff staff) {
        this.staffId = staff.getStaffId();
        this.fullName = staff.getFullName();
        this.dateOfBirth = staff.getDateOfBirth();
        this.gender = staff.getGender();
        this.email = staff.getEmail();
        this.phone = staff.getPhone();
        this.idCard = staff.getIdCard();
        this.position = staff.getPosition();
        this.role = staff.getAccount() != null && staff.getAccount().getRole() != null
                ? staff.getAccount().getRole().name()
                : null;
        this.active = staff.getAccount() != null && staff.getAccount().getStatus() != null
                ? staff.getAccount().getStatus().name().equals("ACTIVE")
                : false;
    }

    // Getters and Setters
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
